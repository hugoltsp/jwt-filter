package com.hugoltsp.spring.boot.starter.jwt.filter.userdetails;

import com.hugoltsp.spring.boot.starter.jwt.filter.JwtAuthenticationFilterException;

@FunctionalInterface
public interface UserDetailsValidator<T extends UserDetails> {

	void validate(T user) throws JwtAuthenticationFilterException;

}
