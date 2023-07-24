package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class OpenfeignHttpclientSSLApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenfeignHttpclientSSLApplication.class, args);
    }

}
