package com.hugoltsp.spring.boot.starter.jwt.filter;

import java.util.Optional;

public final class AuthenticationContextHolder {

    private static final ThreadLocal<AuthenticationContext> CONTEXT = new ThreadLocal<>();

    private AuthenticationContextHolder() {

    }

    public static Optional<AuthenticationContext> getCurrent() {

        return Optional.ofNullable(CONTEXT.get());
    }

    public static void set(AuthenticationContext authenticationContext) {

        CONTEXT.set(authenticationContext);
    }

}
