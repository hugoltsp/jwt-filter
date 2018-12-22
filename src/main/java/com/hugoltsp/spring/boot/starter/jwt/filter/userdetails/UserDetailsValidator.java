package com.hugoltsp.spring.boot.starter.jwt.filter.userdetails;

@FunctionalInterface
public interface UserDetailsValidator {

	void validate(UserDetails user);

}
