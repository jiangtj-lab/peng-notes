package com.peng.daliy.dynamicconfig.client.listener;

import com.peng.daliy.dynamicconfig.client.properties.PlaceholderHelper;
import com.peng.daliy.dynamicconfig.client.properties.SpringValue;
import com.peng.daliy.dynamicconfig.client.properties.SpringValueRegistry;
import com.peng.daliy.dynamicconfig.client.utils.SpringInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;


@Component
public class PropertiesRefreshedListener implements ApplicationListener<EnvironmentChangeEvent> {

	private static final Logger log = LoggerFactory.getLogger(PropertiesRefreshedListener.class);
	private final boolean typeConverterHasConvertIfNecessaryWithFieldParameter;
	private final ConfigurableBeanFactory beanFactory;
	private final PlaceholderHelper placeholderHelper;
	private final SpringValueRegistry springValueRegistry;
	private final TypeConverter typeConverter;

	public PropertiesRefreshedListener(ConfigurableListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		this.placeholderHelper = SpringInjector.getInstance(PlaceholderHelper.class);
		this.springValueRegistry = SpringInjector.getInstance(SpringValueRegistry.class);
		this.typeConverter = this.beanFactory.getTypeConverter();
		this.typeConverterHasConvertIfNecessaryWithFieldParameter = testTypeConverterHasConvertIfNecessaryWithFieldParameter();
	}

	@Override
	public void onApplicationEvent(EnvironmentChangeEvent environmentChangeEvent) {
		Map<String, Object> data = environmentChangeEvent.getData();
		log.info("refresh data is :" + data);
		Set<String> keys = data.keySet();
		if (CollectionUtils.isEmpty(keys)) {
			return;
		}
		for (String key : keys) {
			// 1. check if the changed key is exits
			Collection<SpringValue> targetValues = springValueRegistry.get(beanFactory, key);
			if (targetValues == null || targetValues.isEmpty()) {
				continue;
			}
			// 2. update the value
			for (SpringValue val : targetValues) {
				updateSpringValue(val);
			}
		}
	}

	private void updateSpringValue(SpringValue springValue) {
		try {
			Object value = resolvePropertyValue(springValue);
			springValue.update(value);
			log.info("Auto update diconf changed value successfully, new value: {}, {}", value, springValue);
		} catch (Throwable ex) {
			log.error("Auto update diconf changed value failed, {}", springValue.toString(), ex);
		}
	}

	private Object resolvePropertyValue(SpringValue springValue) {
		// value will never be null, as @Value
		Object value = placeholderHelper.resolvePropertyValue(beanFactory, springValue.getBeanName(),
				springValue.getPlaceholder());
		if (springValue.isField()) {
			// org.springframework.beans.TypeConverter#convertIfNecessary(java.lang.Object,
			// java.lang.Class, java.lang.reflect.Field) is available from Spring 3.2.0+
			if (typeConverterHasConvertIfNecessaryWithFieldParameter) {
				value = this.typeConverter.convertIfNecessary(value, springValue.getTargetType(),
						springValue.getField());
			} else {
				value = this.typeConverter.convertIfNecessary(value, springValue.getTargetType());
			}
		} else {
			value = this.typeConverter.convertIfNecessary(value, springValue.getTargetType(),
					springValue.getMethodParameter());
		}
		return value;
	}

	private boolean testTypeConverterHasConvertIfNecessaryWithFieldParameter() {
		try {
			TypeConverter.class.getMethod("convertIfNecessary", Object.class, Class.class, Field.class);
		} catch (Throwable ex) {
			return false;
		}
		return true;
	}
}
