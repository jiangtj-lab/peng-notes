package com.peng.daliy.dynamicconfig.client.conn;

import com.peng.daliy.dynamicconfig.client.helper.DynamicconfigHelper;
import com.peng.daliy.dynamicconfig.client.helper.RestTemplateHelper;
import com.peng.daliy.dynamicconfig.client.model.Dynamicconfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class DynamicconfigCommandLineRunner implements CommandLineRunner, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(DynamicconfigCommandLineRunner.class);


    private RestTemplate restTemplate;

    @Resource
    private ConfigurableEnvironment environment;

    private ApplicationContext applicationContext;


    public DynamicconfigCommandLineRunner() {
        this.restTemplate = RestTemplateHelper.builer();
    }

    @Override
    public void run(String... args) throws Exception {

        MutablePropertySources propertySources = environment.getPropertySources();
        Thread longPollThread = new Thread(new LongPollDisconf(environment, propertySources));
        log.info("longPollThread start");
        longPollThread.start();
    }


    class LongPollDisconf implements Runnable {

        private MutablePropertySources propertySources;

        private ConfigurableEnvironment environment;

        public LongPollDisconf(ConfigurableEnvironment environment, MutablePropertySources propertySources) {
            this.propertySources = propertySources;
            this.environment = environment;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Dynamicconfig dynamicconfig = DynamicconfigHelper.initDynamicconfig(environment);
                    Map<String, Object> remoteConfig = DynamicconfigHelper.loadRemoteConfig(dynamicconfig, restTemplate);
                    DynamicconfigHelper.refreshPropertiesSource(remoteConfig, propertySources, applicationContext);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // e.printStackTrace();
                    log.warn("获取配置失败，原因: {}", e.getMessage());
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;


    }
}
