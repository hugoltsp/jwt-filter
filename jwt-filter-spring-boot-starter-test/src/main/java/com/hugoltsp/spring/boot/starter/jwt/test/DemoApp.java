package com.hugoltsp.spring.boot.starter.jwt.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RequestMapping("/")
@RestController
@SpringBootApplication
public class DemoApp {

    @Autowired
    RequestMappingHandlerMapping requestMappingHandlerMapping;

    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class, args);
    }

    @GetMapping("/test")
    public String publicUrl() {
        return "Hey :)";
    }

    @PostMapping("/protected")
    public String privateUrl() {
        return "Hey :)";
    }

    @GetMapping("/protected")
    public String privateUrlGet() {
        return "Hey :)";
    }

}