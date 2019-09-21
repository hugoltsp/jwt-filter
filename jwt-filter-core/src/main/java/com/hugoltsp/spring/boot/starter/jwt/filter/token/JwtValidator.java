package com.hugoltsp.spring.boot.starter.jwt.filter.token;

import com.hugoltsp.spring.boot.starter.jwt.filter.JwtAuthenticationFilterException;

@FunctionalInterface
public interface JwtValidator {

    void validateJwt(String jwt) throws JwtAuthenticationFilterException;

}
