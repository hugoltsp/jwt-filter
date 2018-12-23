package com.hugoltsp.spring.boot.starter.jwt.filter.config;

import com.hugoltsp.spring.boot.starter.jwt.filter.JwtAuthenticationFilter;
import com.hugoltsp.spring.boot.starter.jwt.filter.authentication.AuthenticationContext;
import com.hugoltsp.spring.boot.starter.jwt.filter.authentication.AuthenticationContextFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.setting.JwtAuthenticationSettings;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@ConditionalOnClass(JwtAuthenticationFilter.class)
public class JwtAuthenticationFilterAutoConfiguration {

    private static final String LOG_NO_CUSTOM_BEAN_PROVIDED = "No bean of type [{}] provided, falling back to default implementation.";

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilterAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsValidator noOpUserDetailsValidator() {
        LOGGER.info(LOG_NO_CUSTOM_BEAN_PROVIDED, UserDetailsValidator.class.getSimpleName());
        return (u) -> {
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsFactory noOpUserDetailsFinder() {
        LOGGER.info(LOG_NO_CUSTOM_BEAN_PROVIDED, UserDetailsFactory.class.getSimpleName());
        return c -> Optional.of(() -> c);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationContextFactory simpleAuthenticationContextFactory() {
        LOGGER.info(LOG_NO_CUSTOM_BEAN_PROVIDED, AuthenticationContextFactory.class.getSimpleName());
        return AuthenticationContext::new;
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtAuthenticationSettings jwtAuthenticationSettings) {
        return new JwtAuthenticationFilter(jwtAuthenticationSettings,
                noOpUserDetailsValidator(),
                noOpUserDetailsFinder(),
                simpleAuthenticationContextFactory());
    }

}
