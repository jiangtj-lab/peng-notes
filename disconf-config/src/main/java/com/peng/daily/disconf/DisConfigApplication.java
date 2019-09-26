package com.peng.daily.disconf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages = {"com.peng.daily.disconf"})
public class DisConfigApplication implements CommandLineRunner {

    public static final Logger log = LoggerFactory.getLogger(DisConfigApplication.class);


    @Autowired
    private Environment environment;

    @Value("${disconf.address}")
    private String disconfAddress;


    public static void main(String[] args) {
        SpringApplication.run(DisConfigApplication.class, args);

    }

    public void run(String... args) throws Exception {

        while (true) {
            Thread.sleep(2000);
            log.info(environment.getProperty("disconf.address"));
            log.info(environment.getProperty("spring.redis.database"));
            log.info(disconfAddress);
        }

    }


}
