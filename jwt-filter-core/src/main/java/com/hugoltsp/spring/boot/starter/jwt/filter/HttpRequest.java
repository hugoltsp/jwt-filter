package com.hugoltsp.spring.boot.starter.jwt.filter;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

public class HttpRequest {

    private final String method;
    private final String requestUri;
    private final String authorizationHeader;

    HttpRequest(HttpServletRequest httpServletRequest) {
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

    public String extractToken() {

        return Optional.ofNullable(getAuthorizationHeader())
                .filter(this::isHeaderValid)
                .map(this::parseToken)
                .orElseThrow(() -> new JwtAuthenticationFilterException("No JWT provided in Authorization Header."));
    }

    private boolean isHeaderValid(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    private String parseToken(String authorizationHeader) {

        String token = null;

        String[] splitHeader = authorizationHeader.split(" ");

        if (splitHeader.length == 2) {
            token = splitHeader[1];
        }

        return token;
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
