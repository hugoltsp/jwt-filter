package com.hugoltsp.spring.boot.starter.jwt.filter;

import org.springframework.http.HttpMethod;

import java.util.List;

class PublicResourceWrapper {

    final PublicResource publicResource;

    PublicResourceWrapper(PublicResource publicResource) {
        this.publicResource = publicResource;
    }

    boolean isPublic(HttpRequest httpRequest) {

        return httpMethodMatches(httpRequest.getMethod()) && urlMatches(httpRequest);
    }

    private boolean httpMethodMatches(String method) {

        return getMethod() == null || getMethod().matches(method);
    }

    private boolean urlMatches(HttpRequest httpRequest) {

        return getUrls().stream().anyMatch(url -> AntMatcher.matches(url, httpRequest.getRequestUri()));
    }

    private HttpMethod getMethod() {
        return publicResource.getMethod();
    }

    private List<String> getUrls() {
        return publicResource.getUrls();
    }

}
