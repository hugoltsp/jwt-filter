package com.hugoltsp.spring.boot.starter.jwt.filter.request;

public interface HttpRequestMatcher {

    boolean isPublic(HttpRequest httpRequest);

}
