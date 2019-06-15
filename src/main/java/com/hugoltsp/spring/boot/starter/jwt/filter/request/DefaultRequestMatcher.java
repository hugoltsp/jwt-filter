package com.hugoltsp.spring.boot.starter.jwt.filter.request;

import com.hugoltsp.spring.boot.starter.jwt.filter.setting.JwtAuthenticationSettings.PublicResource;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DefaultRequestMatcher implements RequestMatcher {

    private final List<PublicResource> publicResources;

    public DefaultRequestMatcher(List<PublicResource> publicResources) {
        this.publicResources = publicResources;
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

        public static boolean computeIfAbsent(HttpRequest httpRequest, Function<HttpRequest, Boolean> function) {

            return MEMOIZED_PUBLIC_HTTP_REQUESTS.contains(httpRequest) || apply(httpRequest, function);
        }

        private static boolean apply(HttpRequest httpRequest, Function<HttpRequest, Boolean> function) {

            boolean isPublic = function.apply(httpRequest);

            if (isPublic) {
                MEMOIZED_PUBLIC_HTTP_REQUESTS.add(httpRequest);
            }

            return isPublic;
        }

    }

}
