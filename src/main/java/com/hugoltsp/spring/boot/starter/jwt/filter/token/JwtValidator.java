package com.hugoltsp.spring.boot.starter.jwt.filter.token;

@FunctionalInterface
public interface JwtValidator {

    void validateJwt(String jwt);

}
