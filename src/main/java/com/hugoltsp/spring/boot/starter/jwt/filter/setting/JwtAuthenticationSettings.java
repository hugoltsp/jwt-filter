package com.hugoltsp.spring.boot.starter.jwt.filter.setting;

import com.hugoltsp.spring.boot.starter.jwt.filter.util.AntMatcherUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class JwtAuthenticationSettings {

    private final String secretKey;

    private final List<PublicResource> publicResources;

    public JwtAuthenticationSettings(String secretKey,
                                     PublicResource... publicResources) {

        if (StringUtils.isEmpty(secretKey)) {
            throw new IllegalArgumentException(String.format(
                    "Illegal secretKey: [%s], it cannot be empty or null.", secretKey));
        }

        this.secretKey = secretKey;
        this.publicResources = publicResources == null ? Collections.emptyList()
                : Arrays.asList(publicResources);
    }

    public String getSecretKey() {
        return secretKey;
    }

    public boolean isPublic(HttpServletRequest request) {

        return isPreFlight(request) || isPublicResource(request);
    }

    private boolean isPreFlight(HttpServletRequest request) {

        return HttpMethod.OPTIONS.matches(request.getMethod());
    }

    private boolean isPublicResource(HttpServletRequest request) {

        return !publicResources.isEmpty()
                && publicResources.stream().anyMatch(r -> r.isPublic(request));
    }

    public static class PublicResource {

        private final HttpMethod method;

        private final List<String> urls;

        public PublicResource(HttpMethod method, List<String> urls) {
            this.method = method;
            this.urls = urls;
        }

        public boolean isPublic(HttpServletRequest request) {

            return httpMethodMatches(request.getMethod()) && urlMatches(request);
        }

        private boolean httpMethodMatches(String method) {

            return this.method == null ? true : this.method.matches(method);
        }

        private boolean urlMatches(HttpServletRequest request) {

            return urls.stream().anyMatch(
                    url -> AntMatcherUtil.matches(url, request.getRequestURI()));
        }

    }

}
