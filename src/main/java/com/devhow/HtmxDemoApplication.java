package com.devhow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
@ConfigurationPropertiesScan
public class HtmxDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HtmxDemoApplication.class, args);
    }

}
