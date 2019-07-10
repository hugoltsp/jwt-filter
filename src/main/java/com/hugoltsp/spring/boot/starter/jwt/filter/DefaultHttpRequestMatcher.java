package com.hugoltsp.spring.boot.starter.jwt.filter;

import com.hugoltsp.spring.boot.starter.jwt.filter.matcher.HttpRequestMatcher;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

class DefaultHttpRequestMatcher implements HttpRequestMatcher {

    private final List<PublicResourceWrapper> publicResources;

    public DefaultHttpRequestMatcher(List<PublicResourceWrapper> publicResources) {
        this.publicResources = new ArrayList<>(publicResources);
    }

    @Override
    public boolean isPublic(HttpRequest httpRequest) {

        return isPreFlight(httpRequest) || Memoizer.computeIfAbsent(httpRequest, this::matchAnyPublicResource);
    }

    private boolean isPreFlight(HttpRequest httpRequest) {

        return HttpMethod.OPTIONS.matches(httpRequest.getMethod());
    }

    private boolean matchAnyPublicResource(HttpRequest httpRequest) {

        return !publicResources.isEmpty() && publicResources.stream().anyMatch(r -> r.isPublic(httpRequest));
    }

    private static final class Memoizer {

        static final Set<HttpRequest> MEMOIZED_PUBLIC_HTTP_REQUESTS = ConcurrentHashMap.newKeySet();

        static boolean computeIfAbsent(HttpRequest httpRequest, Predicate<HttpRequest> predicate) {

            return MEMOIZED_PUBLIC_HTTP_REQUESTS.contains(httpRequest) || test(httpRequest, predicate);
        }

        static boolean test(HttpRequest httpRequest, Predicate<HttpRequest> predicate) {

            boolean isPublic = predicate.test(httpRequest);

            if (isPublic) {
                MEMOIZED_PUBLIC_HTTP_REQUESTS.add(httpRequest);
            }

            return isPublic;
        }

    }

}
