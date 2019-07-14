package com.hugoltsp.spring.boot.starter.jwt.filter;

import com.hugoltsp.spring.boot.starter.jwt.filter.request.HttpRequestMatcher;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

class DefaultHttpRequestMatcher implements HttpRequestMatcher {

    private final SimpleFifoHttpRequestCache cache = new SimpleFifoHttpRequestCache(256);

    private final List<PublicResourceWrapper> publicResources;

    DefaultHttpRequestMatcher(List<PublicResourceWrapper> publicResources) {
        this.publicResources = new ArrayList<>(publicResources);
    }

    @Override
    public boolean isPublic(HttpRequest httpRequest) {
        return isPreFlight(httpRequest) || cache.computeIfAbsent(httpRequest, this::matchAnyPublicResource);
    }

    private boolean isPreFlight(HttpRequest httpRequest) {

        return HttpMethod.OPTIONS.matches(httpRequest.getMethod());
    }

    private boolean matchAnyPublicResource(HttpRequest httpRequest) {

        return !publicResources.isEmpty() && publicResources.stream().anyMatch(r -> r.isPublic(httpRequest));
    }

}
