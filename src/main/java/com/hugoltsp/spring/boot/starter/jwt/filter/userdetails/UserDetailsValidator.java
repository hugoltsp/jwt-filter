package com.hugoltsp.spring.boot.starter.jwt.filter.userdetails;

@FunctionalInterface
public interface UserDetailsValidator<T extends UserDetails> {

	void validate(T user);

}
