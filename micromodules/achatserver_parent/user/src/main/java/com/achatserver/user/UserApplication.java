package com.achatserver.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import util.AchatUtil;
import util.IdWorker;
import util.Jwt;

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
    public AchatUtil achatUtil(){
        return new AchatUtil();
    }

    @Bean
    public Jwt jwt(){
        return new Jwt();
    }
}
