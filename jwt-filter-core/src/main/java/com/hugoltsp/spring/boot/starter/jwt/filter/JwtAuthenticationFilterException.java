package com.hugoltsp.spring.boot.starter.jwt.filter;

public class JwtAuthenticationFilterException extends RuntimeException {

    JwtAuthenticationFilterException(String message) {
        super(message);
    }

}
