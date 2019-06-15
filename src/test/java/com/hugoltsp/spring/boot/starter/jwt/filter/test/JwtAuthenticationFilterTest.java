package com.hugoltsp.spring.boot.starter.jwt.filter.test;

import com.hugoltsp.spring.boot.starter.jwt.filter.JwtAuthenticationFilter;
import com.hugoltsp.spring.boot.starter.jwt.filter.authentication.AuthenticationContextFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.parser.DefaultJwtParser;
import com.hugoltsp.spring.boot.starter.jwt.filter.request.DefaultRequestMatcher;
import com.hugoltsp.spring.boot.starter.jwt.filter.request.HttpRequest;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetails;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsValidator;
import com.hugoltsp.spring.boot.starter.jwt.filter.util.AuthorizationHeaderUtil;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private DefaultJwtParser jwtParser;

    @Mock
    private DefaultRequestMatcher requestMatcher;

    @Mock
    private UserDetailsValidator userDetailsValidator;

    @Mock
    private UserDetailsFactory userDetailsFactory;

    @Mock
    private AuthenticationContextFactory authenticationContextFactory;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    public void should_return_200_when_request_is_deemed_public() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();

        when(requestMatcher.isPublic(new HttpRequest(request))).thenReturn(true);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(SC_OK);

        verify(jwtParser, never()).parse(any());
        verify(userDetailsFactory, never()).createByClaims(any());
        verify(userDetailsValidator, never()).validate(any());
        verify(authenticationContextFactory, never()).create(any());
    }

    @Test
    public void should_return_200_when_request_is_protected_and_user_is_authenticated()
            throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(GET.name());
        request.setRequestURI("/test");
        request.addHeader(AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdWIiLCJuYW1lIjoiSm9obiBEb2UifQ.jKexo6MlFW78w31biGfZGqaf3LRY3KZKMuXJFtkCJ6k");
        HttpRequest httpRequest = new HttpRequest(request);

        DefaultClaims defaultClaims = new DefaultClaims();

        when(requestMatcher.isPublic(httpRequest)).thenReturn(false);
        when(jwtParser.parse(AuthorizationHeaderUtil.extractToken(httpRequest))).thenReturn(defaultClaims);
        when(userDetailsFactory.createByClaims(defaultClaims)).thenReturn(Optional.empty());

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(SC_OK);

        verify(userDetailsValidator, never()).validate(any());
        verify(authenticationContextFactory, only()).create(any());
    }

    @Test
    public void should_return_200_and_validate_user_when_request_is_protected_and_user_is_authenticated_and_userdetails_is_present()
            throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(GET.name());
        request.setRequestURI("/test");
        request.addHeader(AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdWIiLCJuYW1lIjoiSm9obiBEb2UifQ.jKexo6MlFW78w31biGfZGqaf3LRY3KZKMuXJFtkCJ6k");
        HttpRequest httpRequest = new HttpRequest(request);

        DefaultClaims defaultClaims = new DefaultClaims();

        when(requestMatcher.isPublic(httpRequest)).thenReturn(false);
        when(jwtParser.parse(AuthorizationHeaderUtil.extractToken(httpRequest))).thenReturn(defaultClaims);
        when(userDetailsFactory.createByClaims(defaultClaims)).thenReturn(Optional.of(Mockito.mock(UserDetails.class)));

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(SC_OK);

        verify(userDetailsValidator, only()).validate(any());
        verify(authenticationContextFactory, only()).create(any());
    }

    @Test
    public void should_return_401_and_validate_user_when_request_is_protected_and_user_is_authenticated_and_userdetails_is_not_valid()
            throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(GET.name());
        request.setRequestURI("/test");
        request.addHeader(AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdWIiLCJuYW1lIjoiSm9obiBEb2UifQ.jKexo6MlFW78w31biGfZGqaf3LRY3KZKMuXJFtkCJ6k");
        HttpRequest httpRequest = new HttpRequest(request);

        DefaultClaims defaultClaims = new DefaultClaims();
        UserDetails mock = mock(UserDetails.class);

        when(requestMatcher.isPublic(httpRequest)).thenReturn(false);
        when(jwtParser.parse(AuthorizationHeaderUtil.extractToken(httpRequest))).thenReturn(defaultClaims);
        when(userDetailsFactory.createByClaims(defaultClaims)).thenReturn(Optional.of(mock));
        doThrow(new RuntimeException()).when(userDetailsValidator).validate(mock);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(SC_UNAUTHORIZED);

        verify(authenticationContextFactory, never()).create(any());
    }

    @Test
    public void should_return_401_when_request_is_protected() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();

        when(requestMatcher.isPublic(new HttpRequest(request))).thenReturn(false);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(SC_UNAUTHORIZED);
    }

}
