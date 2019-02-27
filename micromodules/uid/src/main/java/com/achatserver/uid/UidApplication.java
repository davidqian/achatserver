package com.achatserver.uid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class UidApplication {
    public static void main(String[] args) {
        SpringApplication.run(UidApplication.class, args);
    }
}
