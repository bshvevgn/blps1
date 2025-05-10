package com.eg.blps1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Blps1Application {

    public static void main(String[] args) {
        SpringApplication.run(Blps1Application.class, args);
    }

}
