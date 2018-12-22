package com.hugoltsp.spring.boot.starter.jwt.filter.authentication;

import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetails;

import java.util.Optional;

public class AuthenticationContext {

    private final Optional<UserDetails> userDetails;

    public AuthenticationContext(Optional<UserDetails> userDetails) {
        this.userDetails = userDetails;
    }

    public Optional<UserDetails> getUserDetails() {
        return userDetails;
    }

}