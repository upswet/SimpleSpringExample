package ru.app.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    @Secured({"ROLE_HELLO"})
    public String helloService(){
        return "my hello";
    }
}
