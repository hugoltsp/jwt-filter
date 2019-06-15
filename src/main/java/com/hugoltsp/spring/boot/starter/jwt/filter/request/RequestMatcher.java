package com.hugoltsp.spring.boot.starter.jwt.filter.request;

public interface RequestMatcher {

    boolean isPublic(HttpRequest servletHttpRequest);

}
