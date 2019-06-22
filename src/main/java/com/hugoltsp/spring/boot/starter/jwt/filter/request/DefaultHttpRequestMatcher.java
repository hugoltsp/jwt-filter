package com.hugoltsp.spring.boot.starter.jwt.filter.request;

import com.hugoltsp.spring.boot.starter.jwt.filter.setting.JwtAuthenticationSettings.PublicResource;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class DefaultHttpRequestMatcher implements HttpRequestMatcher {

    private final List<PublicResource> publicResources;

    public DefaultHttpRequestMatcher(List<PublicResource> publicResources) {
        this.publicResources = new ArrayList<>(publicResources);
        this.publicResources.removeIf(Objects::isNull);
    }

    @Override
    public boolean isPublic(HttpRequest httpRequest) {

        return isPreFlight(httpRequest) || Memoizer.computeIfAbsent(httpRequest, this::isPublicResource);
    }

    private boolean isPreFlight(HttpRequest httpRequest) {

        return HttpMethod.OPTIONS.matches(httpRequest.getMethod());
    }

    private boolean isPublicResource(HttpRequest httpRequest) {

        return !publicResources.isEmpty() && publicResources.stream().anyMatch(r -> r.isPublic(httpRequest));
    }

    private static final class Memoizer {

        private static final Set<HttpRequest> MEMOIZED_PUBLIC_HTTP_REQUESTS = ConcurrentHashMap.newKeySet();

        public static boolean computeIfAbsent(HttpRequest httpRequest, Predicate<HttpRequest> predicate) {

            return MEMOIZED_PUBLIC_HTTP_REQUESTS.contains(httpRequest) || test(httpRequest, predicate);
        }

        private static boolean test(HttpRequest httpRequest, Predicate<HttpRequest> predicate) {

            boolean isPublic = predicate.test(httpRequest);

            if (isPublic) {
                MEMOIZED_PUBLIC_HTTP_REQUESTS.add(httpRequest);
            }

            return isPublic;
        }

    }

}
