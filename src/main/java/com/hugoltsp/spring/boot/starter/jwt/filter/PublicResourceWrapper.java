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

    boolean httpMethodMatches(String method) {

        return getMethod() == null || getMethod().matches(method);
    }

    boolean urlMatches(HttpRequest httpRequest) {

        return getUrls().stream().anyMatch(url -> AntMatcherUtil.matches(url, httpRequest.getRequestUri()));
    }

    HttpMethod getMethod() {
        return publicResource.getMethod();
    }

    List<String> getUrls() {
        return publicResource.getUrls();
    }

}
