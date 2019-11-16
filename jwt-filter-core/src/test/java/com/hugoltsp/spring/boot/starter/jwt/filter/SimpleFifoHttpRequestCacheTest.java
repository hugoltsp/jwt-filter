package com.hugoltsp.spring.boot.starter.jwt.filter;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Random;

import static java.util.UUID.randomUUID;
import static java.util.stream.Stream.generate;
import static org.assertj.core.api.Assertions.assertThat;

public class SimpleFifoHttpRequestCacheTest {

    @Test
    public void computeIfAbsent_should_evict_entries_when_size_exceeds() {
        SimpleFifoHttpRequestCache simpleFifoHttpRequestCache = new SimpleFifoHttpRequestCache(256);

        generate(this::createRequest)
                .parallel()
                .limit(1024)
                .forEach(r -> simpleFifoHttpRequestCache.computeIfAbsent(r, it -> true));

        assertThat(simpleFifoHttpRequestCache.size()).isEqualTo(256);
    }

    private HttpRequest createRequest() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setMethod(method());
        mockHttpServletRequest.setRequestURI(randomUUID().toString());
        return new HttpRequest(mockHttpServletRequest);
    }

    private String method() {
        HttpMethod[] values = HttpMethod.values();
        int length = values.length;
        return values[new Random().nextInt(length)].name();
    }

}