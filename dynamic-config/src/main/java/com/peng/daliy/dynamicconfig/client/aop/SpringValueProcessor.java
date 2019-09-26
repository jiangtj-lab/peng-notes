package com.peng.daliy.dynamicconfig.client.aop;

import com.peng.daliy.dynamicconfig.client.properties.PlaceholderHelper;
import com.peng.daliy.dynamicconfig.client.properties.SpringValue;
import com.peng.daliy.dynamicconfig.client.properties.SpringValueRegistry;
import com.peng.daliy.dynamicconfig.client.utils.SpringInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Spring value processor of field or method which has @Value and xml config
 * placeholders.
 *
 */
@Component
public class SpringValueProcessor extends DynamicconfigProcessor implements BeanFactoryAware {

	private static final Logger logger = LoggerFactory.getLogger(SpringValueProcessor.class);
	private final PlaceholderHelper placeholderHelper;
	private final SpringValueRegistry springValueRegistry;
	private BeanFactory beanFactory;

	public SpringValueProcessor() {
		placeholderHelper = SpringInjector.getInstance(PlaceholderHelper.class);
		springValueRegistry = SpringInjector.getInstance(SpringValueRegistry.class);
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return super.postProcessBeforeInitialization(bean, beanName);
//		return bean;
	}

	@Override
	protected void processField(Object bean, String beanName, Field field) {
		// register @Value on field
		Value value = field.getAnnotation(Value.class);
		if (value == null) {
			return;
		}
		Set<String> keys = placeholderHelper.extractPlaceholderKeys(value.value());
		if (keys.isEmpty()) {
			return;
		}
		for (String key : keys) {
			SpringValue springValue = new SpringValue(key, value.value(), bean, beanName, field, false);
			springValueRegistry.register(beanFactory, key, springValue);
			logger.info("Monitoring {}", springValue);
		}
	}

	@Override
	protected void processMethod(Object bean, String beanName, Method method) {
		// register @Value on method
		Value value = method.getAnnotation(Value.class);
		if (value == null) {
			return;
		}
		// skip Configuration bean methods
		if (method.getAnnotation(Bean.class) != null) {
			return;
		}
		if (method.getParameterTypes().length != 1) {
			logger.error("Ignore @Value setter {}.{}, expecting 1 parameter, actual {} parameters",
					bean.getClass().getName(), method.getName(), method.getParameterTypes().length);
			return;
		}
		Set<String> keys = placeholderHelper.extractPlaceholderKeys(value.value());
		if (keys.isEmpty()) {
			return;
		}
		for (String key : keys) {
			SpringValue springValue = new SpringValue(key, value.value(), bean, beanName, method, false);
			springValueRegistry.register(beanFactory, key, springValue);
			logger.info("Monitoring {}", springValue);
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
