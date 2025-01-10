package ru.app.gen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//https://www.baeldung.com/spring-cors

/**Разрешим CORS-запросы на этот веб-сервер чтобы сваггер с шлюза мог успешно перенаправлять запросы сюда*/
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    /**Разрешаем CORS для сваггера*/
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
        //registry.addMapping("/api-docs"); //только для сваггера
    }
}