package com.hugoltsp.spring.boot.starter.jwt.filter;

import org.junit.Before;
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
    private PublicResourceWrapper publicResourceWrapper;

    private DefaultHttpRequestMatcher defaultHttpRequestMatcher;

    @Before
    public void setup() {
        defaultHttpRequestMatcher = new DefaultHttpRequestMatcher(singletonList(publicResourceWrapper));
    }

    @Test
    public void isPublic_should_return_true_if_method_is_OPTIONS() {
        assertThat(defaultHttpRequestMatcher.isPublic(request(OPTIONS, "/"))).isTrue();
    }

    @Test
    public void isPublic_should_return_true_if_public_resource_match_request() {
        HttpRequest request = request(GET, "/user");
        when(publicResourceWrapper.isPublic(request)).thenReturn(true);

        assertThat(defaultHttpRequestMatcher.isPublic(request)).isTrue();
    }

    @Test
    public void isSecured_should_return_false_if_public_resource_match_request() {
        HttpRequest request = request(GET, "/user");
        when(publicResourceWrapper.isPublic(request)).thenReturn(true);

        assertThat(defaultHttpRequestMatcher.isSecured(request)).isFalse();
    }

    @Test
    public void isPublic_should_return_false_if_public_resource_does_not_match_request() {
        HttpRequest request = request(GET, "/user");
        when(publicResourceWrapper.isPublic(request)).thenReturn(false);

        assertThat(defaultHttpRequestMatcher.isPublic(request)).isFalse();
    }

    private HttpRequest request(HttpMethod httpMethod, String url) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(httpMethod.name());
        request.setRequestURI(url);
        return new HttpRequest(request);
    }

}