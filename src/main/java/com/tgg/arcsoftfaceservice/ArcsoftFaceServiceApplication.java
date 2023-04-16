package com.tgg.arcsoftfaceservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.tgg.arcsoftfaceservice.mapper"})
public class ArcsoftFaceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArcsoftFaceServiceApplication.class, args);
    }

}
