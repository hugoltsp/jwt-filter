package com.hugoltsp.spring.boot.starter.jwt.filter.authentication;

import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetails;
import io.jsonwebtoken.Claims;

import java.util.Optional;

public class AuthenticationContext {

    private static final ThreadLocal<AuthenticationContext> CONTEXT = new ThreadLocal<>();

    private final Claims claims;

    private final Optional<UserDetails> userDetails;

    public AuthenticationContext(Optional<UserDetails> userDetails, Claims claims) {
        this.claims = claims;
        this.userDetails = userDetails;
    }

    public Claims getClaims() {
        return claims;
    }

    public Optional<UserDetails> getUserDetails() {
        return userDetails;
    }

    public static AuthenticationContext getCurrent() {

        return CONTEXT.get();
    }

    public static void set(AuthenticationContext authenticationContext) {

        CONTEXT.set(authenticationContext);

    }

}
