package ru.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import ru.app.db.service.GenerateDataInDbService;
import ru.app.utils.ApplicationContextProvider;

//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class })
//выкл автоконфигурацию для спринг-секьюрити см https://www.baeldung.com/spring-boot-security-autoconfiguration
@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);

        ApplicationContextProvider.getBean(GenerateDataInDbService.class).generate();
    }
}
