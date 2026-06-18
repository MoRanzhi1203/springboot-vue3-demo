package com.example.springbootdemobackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.springbootdemobackend.mapper")
public class SpringbootDemoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDemoBackendApplication.class, args);
    }

}
