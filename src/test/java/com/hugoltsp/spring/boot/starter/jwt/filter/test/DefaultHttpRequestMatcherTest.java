package com.hugoltsp.spring.boot.starter.jwt.filter.test;

import com.hugoltsp.spring.boot.starter.jwt.filter.request.DefaultHttpRequestMatcher;
import com.hugoltsp.spring.boot.starter.jwt.filter.request.HttpRequest;
import com.hugoltsp.spring.boot.starter.jwt.filter.setting.JwtAuthenticationSettings.PublicResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;

@RunWith(MockitoJUnitRunner.class)
public class DefaultHttpRequestMatcherTest {

    @Mock
    private PublicResource publicResource;

    private DefaultHttpRequestMatcher defaultHttpRequestMatcher;

    @Test
    public void isPublic_should_return_true_if_method_is_OPTIONS() {
        assertThat(requestMatcher().isPublic(request(OPTIONS, "/"))).isTrue();
    }

    @Test
    public void isPublic_should_return_true_if_public_resource_match_request() {
        HttpRequest request = request(GET, "/user");
        when(publicResource.isPublic(request)).thenReturn(true);

        assertThat(requestMatcher().isPublic(request)).isTrue();
    }

    @Test
    public void isPublic_should_return_false_if_public_resource_does_not_match_request() {
        HttpRequest request = request(GET, "/user");
        when(publicResource.isPublic(request)).thenReturn(false);

        assertThat(requestMatcher().isPublic(request)).isFalse();
    }

    private HttpRequest request(HttpMethod httpMethod, String url) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(httpMethod.name());
        request.setRequestURI(url);
        return new HttpRequest(request);
    }

    private DefaultHttpRequestMatcher requestMatcher() {
        defaultHttpRequestMatcher = new DefaultHttpRequestMatcher(singletonList(publicResource));
        return defaultHttpRequestMatcher;
    }

}