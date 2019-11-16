package com.hugoltsp.spring.boot.starter.jwt.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class TestWebApp {

    public static void main(String[] args) {
        SpringApplication.run(TestWebApp.class, args);
    }

    @GetMapping("/unprotected")
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