package ru.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
class ProxyConfig {
    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
        /* Как пример настройки
                .route("random_animal_route",
                        route -> route.path("/myauth")
                                .and()
                                .method(HttpMethod.POST)
                                //.filters(filter -> filter.stripPrefix(1))
                                .uri("http://localhost:8079/auth"))
                .route("zoo_route",
                        route -> route.path("/zoo/**")
                                .filters(filter -> filter.stripPrefix(1))
                                .uri("lb://zoo"))*/
                .build();
    }
}