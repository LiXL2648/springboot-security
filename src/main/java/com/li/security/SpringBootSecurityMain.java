package com.li.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.li.security.dao")
public class SpringBootSecurityMain {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityMain.class, args);
    }
}
