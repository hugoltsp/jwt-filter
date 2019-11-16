package com.hugoltsp.spring.boot.starter.jwt.filter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RunWith(SpringJUnit4ClassRunner.class)
public class HttpRequestUtilTest {

    @Rule
    public ExpectedException exceptionRule = none();

    @Test
    public void extractToken_should_return_token_when_request_contains_valid_authorization_header() {

        String expectedToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdWIiLCJuYW1lIjoiSm9obiBEb2UifQ.jKexo6MlFW78w31biGfZGqaf3LRY3KZKMuXJFtkCJ6k";

        MockHttpServletRequest request = createRequest(
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdWIiLCJuYW1lIjoiSm9obiBEb2UifQ.jKexo6MlFW78w31biGfZGqaf3LRY3KZKMuXJFtkCJ6k");

        assertThat(HttpRequestUtil.extractToken(new HttpRequest(request)))
                .isEqualTo(expectedToken);
    }

    @Test
    public void extractToken_should_return_MalformedAuthorizationHeaderException_when_no_valid_authorization_header_is_blank() {

        MockHttpServletRequest request = createRequest("");

        exceptionRule.expect(JwtAuthenticationFilterException.class);

        HttpRequestUtil.extractToken(new HttpRequest(request));
    }

    @Test
    public void extractToken_should_return_MalformedAuthorizationHeaderException_when_no_valid_authorization_header_is_null() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        exceptionRule.expect(JwtAuthenticationFilterException.class);
        HttpRequestUtil.extractToken(new HttpRequest(request));
    }

    @Test
    public void extractToken_should_return_MalformedAuthorizationHeaderException_when_no_valid_authorization_header_has_no_Bearer_prefix() {

        MockHttpServletRequest request = createRequest(" test test 123");

        exceptionRule.expect(JwtAuthenticationFilterException.class);

        HttpRequestUtil.extractToken(new HttpRequest(request));
    }

    @Test
    public void extractToken_should_return_MalformedAuthorizationHeaderException_when_there_is_no_token() {

        MockHttpServletRequest request = createRequest("Bearer ");

        exceptionRule.expect(JwtAuthenticationFilterException.class);

        HttpRequestUtil.extractToken(new HttpRequest(request));
    }

    private static MockHttpServletRequest createRequest(String authorizationHeaderValue) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(AUTHORIZATION, authorizationHeaderValue);
        return request;
    }

}