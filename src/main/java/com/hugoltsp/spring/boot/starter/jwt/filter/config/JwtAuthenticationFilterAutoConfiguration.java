package com.hugoltsp.spring.boot.starter.jwt.filter.config;

import com.hugoltsp.spring.boot.starter.jwt.filter.JwtAuthenticationFilter;
import com.hugoltsp.spring.boot.starter.jwt.filter.authentication.AuthenticationContext;
import com.hugoltsp.spring.boot.starter.jwt.filter.authentication.AuthenticationContextFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.settings.JwtAuthenticationSettings;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsFinder;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Optional.empty;

@Configuration
@ConditionalOnClass(JwtAuthenticationFilter.class)
public class JwtAuthenticationFilterAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilterAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsValidator noOpUserDetailsValidator() {
        LOGGER.info("No bean of type [{}] found", UserDetailsValidator.class.getSimpleName());
        return (u, c) -> {

        };
    }

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsFinder noOpUserDetailsFinder() {
        LOGGER.info("No bean of type [{}] found", UserDetailsFinder.class.getSimpleName());
        return c -> empty();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationContextFactory simpleAuthenticationContextFactory() {
        LOGGER.info("No bean of type [{}] found", AuthenticationContextFactory.class.getSimpleName());
        return AuthenticationContext::new;
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtAuthenticationSettings jwtAuthenticationSettings) {
        return new JwtAuthenticationFilter(jwtAuthenticationSettings,
                noOpUserDetailsValidator(),
                noOpUserDetailsFinder(),
                simpleAuthenticationContextFactory());
    }

}
