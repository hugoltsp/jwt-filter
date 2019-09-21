package com.hugoltsp.spring.boot.starter.jwt.filter;

public class JwtAuthenticationFilterException extends RuntimeException {

    public JwtAuthenticationFilterException(String message) {
        super(message);
    }

}
