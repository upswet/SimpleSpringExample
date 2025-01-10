package ru.app.listener;

//https://habr.com/ru/companies/otus/articles/590761/

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

//https://www.baeldung.com/spring-reloading-properties
//https://habr.com/ru/companies/otus/articles/590761/
/**Пример как отловить выполнение  /actuator/refresh*/
@Service
@RefreshScope
@Slf4j
public class MyRefreshListener implements ApplicationListener<EnvironmentChangeEvent> {
    @Value("${myValue:none}")
    private String myValue;

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        if(event.getKeys().contains("myValue")) {
            log.info("REFRESH! myValue={}",myValue);
        }
    }
}
