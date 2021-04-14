package com.devhow.htmxdemo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration
class SpringContextLoadTests {
    private static final Logger logger = LoggerFactory.getLogger(SpringContextLoadTests.class);
    @Autowired
    private Environment environment;

    @Test
    void contextLoads() {
        for (String profileName : environment.getActiveProfiles()) {
            logger.info("Currently active profile - " + profileName);
        }
    }

    @Configuration
    static class Config {
    }
}
