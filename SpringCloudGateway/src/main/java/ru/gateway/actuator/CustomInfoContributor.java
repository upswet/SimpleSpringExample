package ru.gateway.actuator;

import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

/**Заполняем информацию в инфо*/
@Component
public class CustomInfoContributor implements InfoContributor {

    @Override
    public void contribute(Builder builder) {
        builder
                .withDetail("version", "1.0.0")
                .withDetail("description", "My Spring Boot Application")
                .withDetail("environment", "production");
    }
}
