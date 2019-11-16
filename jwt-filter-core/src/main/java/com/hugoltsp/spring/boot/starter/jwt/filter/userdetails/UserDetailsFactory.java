package com.hugoltsp.spring.boot.starter.jwt.filter.userdetails;

import io.jsonwebtoken.Claims;

import java.util.Optional;

@FunctionalInterface
public interface UserDetailsFactory<T extends UserDetails> {

	Optional<T> createByClaims(Claims claims);

}
