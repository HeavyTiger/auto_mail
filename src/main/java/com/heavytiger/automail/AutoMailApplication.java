package com.heavytiger.automail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AutoMailApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoMailApplication.class, args);
    }

}
