package com.hugoltsp.spring.boot.starter.jwt.filter.matcher;

import com.hugoltsp.spring.boot.starter.jwt.filter.HttpRequest;

@FunctionalInterface
public interface HttpRequestMatcher {

    boolean isPublic(HttpRequest httpRequest);

    default boolean isSecured(HttpRequest httpRequest) {
        return !isPublic(httpRequest);
    }

}
