package ru.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringCloudGateway {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGateway.class, args);
    }
}
