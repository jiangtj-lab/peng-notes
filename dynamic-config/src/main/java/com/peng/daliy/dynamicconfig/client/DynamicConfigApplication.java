package com.peng.daliy.dynamicconfig.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication(scanBasePackages = {"com.peng.daliy.dynamicconfig.client"})
@EnableScheduling
public class DynamicConfigApplication implements CommandLineRunner{

    public static final Logger log = LoggerFactory.getLogger(DynamicConfigApplication.class);

    @Autowired
    Environment environment;

    @Value("${test.v1}")
    private String testv1;

    public static void main(String[] args) {
        SpringApplication.run(DynamicConfigApplication.class, args);
    }


    @Scheduled(cron="*/5 * * * * ?")
    public void printEnv() {
        log.info(environment.getProperty("test.v1"));
        log.info(testv1);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("run");
    }
}
