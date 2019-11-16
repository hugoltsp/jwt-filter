package com.hugoltsp.spring.boot.starter.jwt.filter;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class PublicResource {

    private HttpMethod method;

    private List<String> urls = new ArrayList<>();

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
