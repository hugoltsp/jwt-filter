package com.hugoltsp.spring.boot.starter.jwt.filter.test;

import com.hugoltsp.spring.boot.starter.jwt.filter.JwtAuthenticationFilter;
import com.hugoltsp.spring.boot.starter.jwt.filter.authentication.AuthenticationContextFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.setting.JwtAuthenticationSettings;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsFinder;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtAuthenticationSettings settings;

    @Mock
    private UserDetailsValidator userDetailsValidator;

    @Mock
    private UserDetailsFinder userDetailsFinder;

    @Mock
    private AuthenticationContextFactory authenticationContextFactory;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    public void should_return_ok_when_request_is_deemed_public() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();

        when(settings.isPublic(request)).thenReturn(true);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(SC_OK);
    }

}
