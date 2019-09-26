package com.peng.daliy.dynamicconfig.client.helper;

import com.peng.daliy.dynamicconfig.client.model.Dynamicconfig;
import com.peng.daliy.dynamicconfig.client.listener.EnvironmentChangeEvent;
import com.peng.daliy.dynamicconfig.client.model.DynamicconfigConstants;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

public class DynamicconfigHelper {


    public static Map<String, Object> loadRemoteConfig(Dynamicconfig dynamicconfig, RestTemplate restTemplate) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, null);
        ResponseEntity<String> response = restTemplate.exchange(dynamicconfig.getAddress(), HttpMethod.GET, requestEntity, String.class);
        Map<String, Object> result = processRemoteData(response.getBody());
        return result;
    }


    private static Map<String, Object> processRemoteData(String data) {
        System.out.println("receive remote config data is : " + data);
        JsonParser parser = JsonParserFactory.getJsonParser();
        return parser.parseMap(data);
    }

    public static void refreshPropertiesSource(Map<String, Object> remoteConfig, MutablePropertySources propertySources, ApplicationContext applicationContext) {
        if (remoteConfig != null) {
            propertySources.addFirst(new MapPropertySource("disConf", remoteConfig));
            if (applicationContext != null) {
                applicationContext.publishEvent(new EnvironmentChangeEvent(remoteConfig));
            }
        }


    }

    public static Dynamicconfig initDynamicconfig(ConfigurableEnvironment environment) {
        String address = environment.getProperty(DynamicconfigConstants.DYNAMICCONFIG_ADDRESS);
        String localecachepath = environment.getProperty(DynamicconfigConstants.DYNAMICCONFIG_LOCALECACHEPATH);
        return new Dynamicconfig(address,localecachepath);
    }
}
