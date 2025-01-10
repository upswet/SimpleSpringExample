package ru.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

//https://struchkov.dev/blog/ru/api-swagger/

//описание свагерра
@OpenAPIDefinition(
        info = @Info(
                title = "Тестовое Api",
                description = "Пример описания АПИ",
                version = "1.0.0",
                contact = @Contact(
                        name = "Иван Иванов",
                        email = "ivan@ivanov.ruv",
                        url = "https://ivan.ivanov.ru"
                )
        )
)
//авторизация которую должен исп сваггер
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
/*@SecurityScheme(   //for spring security
        name = "jsessionid",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE,
        paramName = "JSESSIONID"
)*/
public class SwaggerConfig {
}
