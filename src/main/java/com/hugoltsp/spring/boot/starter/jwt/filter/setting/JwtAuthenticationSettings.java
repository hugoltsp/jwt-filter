package com.hugoltsp.spring.boot.starter.jwt.filter.setting;

import com.hugoltsp.spring.boot.starter.jwt.filter.util.AntMatcherUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties("jwt.filter")
public class JwtAuthenticationSettings {

    private String secretKey;

    private List<PublicResource> publicResources = new ArrayList<>();

    @PostConstruct
    private void validate() {

        if (StringUtils.isEmpty(getSecretKey())) {
            throw new IllegalArgumentException(String.format("Illegal secretKey: [%s], it cannot be empty or null.",
                    getSecretKey()));
        }

    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public List<PublicResource> getPublicResources() {
        return publicResources;
    }

    public void setPublicResources(List<PublicResource> publicResources) {
        this.publicResources = publicResources;
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

        return !publicResources.isEmpty() && publicResources.stream().anyMatch(r -> r.isPublic(request));
    }

    public static class PublicResource {

        private HttpMethod method;

        private List<String> urls = new ArrayList<>();

        private boolean isPublic(HttpServletRequest request) {

            return httpMethodMatches(request.getMethod()) && urlMatches(request);
        }

        private boolean httpMethodMatches(String method) {

            return this.method == null || this.method.matches(method);
        }

        private boolean urlMatches(HttpServletRequest request) {

            return urls.stream().anyMatch(url -> AntMatcherUtil.matches(url, request.getRequestURI()));
        }

        public HttpMethod getMethod() {
            return method;
        }

        public void setMethod(HttpMethod method) {
            this.method = method;
        }

        public List<String> getUrls() {
            return urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }
    }

}
