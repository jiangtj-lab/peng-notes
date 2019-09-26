package com.peng.daliy.dynamicconfig.client.processor;

import com.peng.daliy.dynamicconfig.client.helper.DynamicconfigHelper;
import com.peng.daliy.dynamicconfig.client.model.Dynamicconfig;
import com.peng.daliy.dynamicconfig.client.helper.RestTemplateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 在程序上下文创建之前自定义环境变量
 * {@link org.springframework.boot.env.EnvironmentPostProcessor}
 */
public class DynamicconfigEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    public static final Logger logger = LoggerFactory.getLogger(DynamicconfigEnvironmentPostProcessor.class);

    private RestTemplate restTemplate;

    public DynamicconfigEnvironmentPostProcessor() {
        this.restTemplate = RestTemplateHelper.builer();
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();
        Dynamicconfig dynamicconfig = DynamicconfigHelper.initDynamicconfig(environment);
        Map<String, Object> remoteConfig = DynamicconfigHelper.loadRemoteConfig(dynamicconfig,restTemplate);
        if (remoteConfig == null) {
            throw new RuntimeException("remoteConfig is null !");
        }
        DynamicconfigHelper.refreshPropertiesSource(remoteConfig,propertySources,null);
        logger.info("propertySources");

    }



    @Override
    public int getOrder() {
        return 0;
    }
}
