package com.anread.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
public class AnreadAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnreadAdminApplication.class, args);
    }

}
