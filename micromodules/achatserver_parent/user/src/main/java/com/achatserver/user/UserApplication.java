package com.achatserver.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import util.GetClientIp;
import util.IdWorker;

@SpringBootApplication
public class UserApplication {
    public static void main(String[] args) {

        SpringApplication.run(UserApplication.class, args);
    }

    @Bean
    public IdWorker getIdWorker(){

        return new IdWorker(1, 1);
    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GetClientIp getClientIp(){
        return new GetClientIp();
    }
}
