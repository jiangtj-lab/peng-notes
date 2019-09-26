package com.peng.daliy.dynamicconfig.client.utils;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.peng.daliy.dynamicconfig.client.exceptions.DynamicconfigConfigException;
import com.peng.daliy.dynamicconfig.client.properties.PlaceholderHelper;
import com.peng.daliy.dynamicconfig.client.properties.SpringValueRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringInjector {

    private static Logger log = LoggerFactory.getLogger(SpringInjector.class);
    private static volatile Injector s_injector;
    private static final Object lock = new Object();

    private static Injector getInjector() {
        if (s_injector == null) {
            synchronized (lock) {
                if (s_injector == null) {
                    try {
                        s_injector = Guice.createInjector(new SpringModule());
                    } catch (Throwable ex) {
                        DynamicconfigConfigException exception = new DynamicconfigConfigException("Unable to initialize XiaoBao Spring Injector!", ex);
                        log.warn(exception.getMessage());
                        throw exception;
                    }
                }
            }
        }

        return s_injector;
    }

    public static <T> T getInstance(Class<T> clazz) {
        try {
            return getInjector().getInstance(clazz);
        } catch (Throwable ex) {
            log.warn(ex.getMessage());
            throw new DynamicconfigConfigException(String.format("Unable to load instance for %s!", clazz.getName()), ex);
        }
    }

    private static class SpringModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(PlaceholderHelper.class).in(Singleton.class);
            bind(SpringValueRegistry.class).in(Singleton.class);
        }
    }
}
