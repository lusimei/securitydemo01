package com.mieasy.securitydemo01;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@MapperScan("com.mieasy.securitydemo01.mapper")
public class Securitydemo01Application {

    public static void main(String[] args) {
        SpringApplication.run(Securitydemo01Application.class, args);
    }

}
