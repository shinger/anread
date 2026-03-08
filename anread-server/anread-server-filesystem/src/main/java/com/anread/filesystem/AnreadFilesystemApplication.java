package com.anread.filesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AnreadFilesystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnreadFilesystemApplication.class, args);
    }

}
