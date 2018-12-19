package com.hugoltsp.spring.boot.starter.jwt.filter.settings;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hugoltsp.spring.boot.starter.jwt.filter.util.AntMatcherUtil.matches;

@Configuration
public class JwtAuthenticationSettings {

    private final String secretKey;

    private final List<PublicResource> publicResources;

    public JwtAuthenticationSettings(String secretKey, PublicResource... publicResources) {
        this.secretKey = secretKey;
        this.publicResources = Arrays.asList(publicResources);
    }

    public String getSecretKey() {
        return secretKey;
    }

    public List<PublicResource> getPublicResources() {
        return publicResources;
    }

    public boolean isPublic(HttpServletRequest request) {

        return isPreFlight(request) || isPublicResource(request);
    }

    private boolean isPreFlight(HttpServletRequest request) {

        return HttpMethod.OPTIONS.matches(request.getMethod());
    }

    private boolean isPublicResource(HttpServletRequest request) {

        return getPublicResources().stream().anyMatch(r -> r.isPublic(request));
    }

    public static class PublicResource {

        private final HttpMethod method;

        private final List<String> urls = new ArrayList<>();

        public PublicResource(HttpMethod method, List<String> urls) {
            this.method = method;
            this.urls.addAll(urls);
        }

        public boolean isPublic(HttpServletRequest request) {

            return httpMethodMatches(request.getMethod()) && urlMatches(request);
        }

        private boolean httpMethodMatches(String method) {
            return this.method == null ? true : this.method.matches(method);
        }

        private boolean urlMatches(HttpServletRequest request) {

            return urls.stream().anyMatch(url -> matches(url, request.getRequestURI()));
        }

    }

}
