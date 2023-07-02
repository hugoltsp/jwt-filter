package com.hugoltsp.spring.boot.starter.jwt.filter;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class HttpRequestTest {

    @Test
    public void extractToken_should_return_token_when_request_contains_valid_authorization_header() {

        String expectedToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdWIiLCJuYW1lIjoiSm9obiBEb2UifQ.jKexo6MlFW78w31biGfZGqaf3LRY3KZKMuXJFtkCJ6k";

        HttpRequest request = createRequest(
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdWIiLCJuYW1lIjoiSm9obiBEb2UifQ.jKexo6MlFW78w31biGfZGqaf3LRY3KZKMuXJFtkCJ6k");

        assertThat(request.extractToken()).isEqualTo(expectedToken);
    }

    @Test
    public void extractToken_should_return_MalformedAuthorizationHeaderException_when_no_valid_authorization_header_is_blank() {

        HttpRequest request = createRequest("");
        assertThrows(JwtAuthenticationFilterException.class, request::extractToken);
    }

    @Test
    public void extractToken_should_return_MalformedAuthorizationHeaderException_when_no_valid_authorization_header_is_null() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        assertThrows(JwtAuthenticationFilterException.class, new HttpRequest(request)::extractToken);
    }

    @Test
    public void extractToken_should_return_MalformedAuthorizationHeaderException_when_no_valid_authorization_header_has_no_Bearer_prefix() {

        HttpRequest request = createRequest(" test test 123");
        assertThrows(JwtAuthenticationFilterException.class, request::extractToken);
    }

    @Test
    public void extractToken_should_return_MalformedAuthorizationHeaderException_when_there_is_no_token() {

        HttpRequest request = createRequest("Bearer ");

        assertThrows(JwtAuthenticationFilterException.class, request::extractToken);
    }

    private static HttpRequest createRequest(String authorizationHeaderValue) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(AUTHORIZATION, authorizationHeaderValue);
        return new HttpRequest(request);
    }

}