package com.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@ConfigurationPropertiesScan("com.test.properties")
@MapperScan("com.test.mapper")
@EnableEurekaClient
public class Oauth2ServerMain {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerMain.class , args);
    }

}
