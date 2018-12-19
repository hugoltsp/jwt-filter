package com.hugoltsp.spring.boot.starter.jwt.filter.userdetails;

import io.jsonwebtoken.Claims;

@FunctionalInterface
public interface UserDetailsValidator {

    void validate(UserDetails user, Claims claims);

}
