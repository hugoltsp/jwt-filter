package com.hugoltsp.spring.boot.starter.jwt.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
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

}
