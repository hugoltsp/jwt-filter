package com.hugoltsp.spring.boot.starter.jwt.filter.request;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class HttpRequest {

    private final String method;
    private final String requestUri;
    private final String authorizationHeader;

    public HttpRequest(HttpServletRequest httpServletRequest) {
        this.method = httpServletRequest.getMethod();
        this.requestUri = httpServletRequest.getRequestURI();
        this.authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
    }

    public String getMethod() {
        return method;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getAuthorizationHeader() {
        return authorizationHeader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequest httpRequest = (HttpRequest) o;
        return Objects.equals(getMethod(), httpRequest.getMethod()) &&
                Objects.equals(getRequestUri(), httpRequest.getRequestUri());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMethod(), getRequestUri());
    }

}
