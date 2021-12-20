package com.musinsa.assign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.musinsa.assign")
public class MusinsaAssignApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusinsaAssignApplication.class, args);
    }

}
