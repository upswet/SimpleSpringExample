package ru.admin;

import de.codecentric.boot.admin.server.config.AdminServerHazelcastAutoConfiguration;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//https://www.baeldung.com/spring-boot-admin
//https://habr.com/ru/articles/479954/
@SpringBootApplication(exclude = AdminServerHazelcastAutoConfiguration.class)
@EnableAdminServer
public class SpringBootAdminServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdminServer.class, args);
    }
}
