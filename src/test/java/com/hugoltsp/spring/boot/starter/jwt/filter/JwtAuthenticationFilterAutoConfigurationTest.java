package com.hugoltsp.spring.boot.starter.jwt.filter;

import com.hugoltsp.spring.boot.starter.jwt.filter.authentication.AuthenticationContextFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.request.HttpRequestMatcher;
import com.hugoltsp.spring.boot.starter.jwt.filter.token.JwtParser;
import com.hugoltsp.spring.boot.starter.jwt.filter.token.JwtValidator;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationFilterAutoConfigurationTest {

    private final JwtAuthenticationFilterAutoConfiguration autoConfiguration = new JwtAuthenticationFilterAutoConfiguration();

    @Mock
    private JwtAuthenticationSettings settings;

    @Test
    public void noOpJwtValidator(){
        assertThat(autoConfiguration.noOpJwtValidator()).isInstanceOf(JwtValidator.class);
    }

    @Test
    public void jwtParser() {
        when(settings.getSecretKey()).thenReturn("secretKey");
        assertThat(autoConfiguration.jwtParser(settings)).isInstanceOf(JwtParser.class);
    }

    @Test
    public void requestMatcher() {
        when(settings.getPublicResources()).thenReturn(emptyList());
        assertThat(autoConfiguration.requestMatcher(settings)).isInstanceOf(HttpRequestMatcher.class);
    }

    @Test
    public void noOpUserDetailsValidator() {
        assertThat(autoConfiguration.noOpUserDetailsValidator()).isInstanceOf(UserDetailsValidator.class);
    }

    @Test
    public void noOpUserDetailsFactory() {
        assertThat(autoConfiguration.noOpUserDetailsFactory()).isInstanceOf(UserDetailsFactory.class);
    }

    @Test
    public void simpleAuthenticationContextFactory() {
        assertThat(autoConfiguration.simpleAuthenticationContextFactory()).isInstanceOf(AuthenticationContextFactory.class);
    }

}