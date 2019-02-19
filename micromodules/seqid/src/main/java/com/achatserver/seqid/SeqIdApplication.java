package com.achatserver.seqid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SeqIdApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeqIdApplication.class, args);
    }
}
