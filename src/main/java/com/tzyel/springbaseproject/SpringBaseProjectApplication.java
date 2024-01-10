package com.tzyel.springbaseproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringBaseProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBaseProjectApplication.class, args);
    }
}
