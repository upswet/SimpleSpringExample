package ru.app.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**Классс свойств*/
@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppProperties {
    /**УИД-приложения*/
    @Value("${spring.application.name:none}")
    String appName;

    /**Включена ли аутентификация*/
    @Value("${auth.enabled:true}")
    private Boolean enabled;

    public static AppProperties self;

    @PostConstruct
    public void postConstruct(){
        self=this;
    }

}
