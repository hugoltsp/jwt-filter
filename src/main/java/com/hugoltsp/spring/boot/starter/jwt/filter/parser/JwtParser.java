package com.hugoltsp.spring.boot.starter.jwt.filter.parser;

import io.jsonwebtoken.Claims;

@FunctionalInterface
public interface JwtParser {

    Claims parse(String token);

}
