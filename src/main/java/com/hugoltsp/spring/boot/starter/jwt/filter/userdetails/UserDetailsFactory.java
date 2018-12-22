package com.hugoltsp.spring.boot.starter.jwt.filter.userdetails;

import io.jsonwebtoken.Claims;

import java.util.Optional;

@FunctionalInterface
public interface UserDetailsFactory {

	Optional<UserDetails> createByClaims(Claims claims);

}
