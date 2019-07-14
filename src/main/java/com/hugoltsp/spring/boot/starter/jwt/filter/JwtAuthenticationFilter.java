package com.hugoltsp.spring.boot.starter.jwt.filter;

import com.hugoltsp.spring.boot.starter.jwt.filter.authentication.AuthenticationContextFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.request.HttpRequestMatcher;
import com.hugoltsp.spring.boot.starter.jwt.filter.token.JwtParser;
import com.hugoltsp.spring.boot.starter.jwt.filter.token.JwtValidator;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetails;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsValidator;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtValidator jwtValidator;

    private final HttpRequestMatcher httpRequestMatcher;

    private final JwtParser jwtParser;

    private final UserDetailsValidator<UserDetails> userDetailsValidator;

    private final UserDetailsFactory<UserDetails> userDetailsFactory;

    private final AuthenticationContextFactory<UserDetails> authenticationContextFactory;

    private JwtAuthenticationFilter(HttpRequestMatcher httpRequestMatcher,
                                    JwtValidator jwtValidator,
                                    JwtParser jwtParser,
                                    UserDetailsValidator<UserDetails> userDetailsValidator,
                                    UserDetailsFactory<UserDetails> userDetailsFactory,
                                    AuthenticationContextFactory<UserDetails> authenticationContextFactory) {
        this.jwtValidator = jwtValidator;
        this.httpRequestMatcher = httpRequestMatcher;
        this.jwtParser = jwtParser;
        this.userDetailsValidator = userDetailsValidator;
        this.userDetailsFactory = userDetailsFactory;
        this.authenticationContextFactory = authenticationContextFactory;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws IOException, ServletException {

        HttpRequest httpRequest = new HttpRequest(httpServletRequest);

        if (httpRequestMatcher.isPublic(httpRequest)) {

            httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        } else {

            try {

                String jwt = HttpRequestUtil.extractToken(httpRequest);

                jwtValidator.validateJwt(jwt);

                Claims claims = jwtParser.parse(jwt);

                Optional<UserDetails> userDetails = userDetailsFactory.createByClaims(claims);

                userDetails.ifPresent(userDetailsValidator::validate);

                AuthenticationContextHolder.set(authenticationContextFactory.create(userDetails));

            } catch (JwtAuthenticationFilterException e) {
                LOGGER.error("Error.", e);
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
            } catch (Exception e) {
                LOGGER.error("Error.", e);
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    static final class JwtAuthenticationFilterBuilder {

        private JwtValidator jwtValidator;
        private HttpRequestMatcher httpRequestMatcher;
        private JwtParser jwtParser;
        private UserDetailsValidator<UserDetails> userDetailsValidator;
        private UserDetailsFactory<UserDetails> userDetailsFactory;
        private AuthenticationContextFactory<UserDetails> authenticationContextFactory;

        JwtAuthenticationFilterBuilder withJwtValidator(JwtValidator jwtValidator) {
            this.jwtValidator = jwtValidator;
            return this;
        }

        JwtAuthenticationFilterBuilder withHttpRequestMatcher(HttpRequestMatcher httpRequestMatcher) {
            this.httpRequestMatcher = httpRequestMatcher;
            return this;
        }

        JwtAuthenticationFilterBuilder withJwtParser(JwtParser jwtParser) {
            this.jwtParser = jwtParser;
            return this;
        }

        JwtAuthenticationFilterBuilder withUserDetailsValidator(UserDetailsValidator<UserDetails> userDetailsValidator) {
            this.userDetailsValidator = userDetailsValidator;
            return this;
        }

        JwtAuthenticationFilterBuilder withUserDetailsFactory(UserDetailsFactory<UserDetails> userDetailsFactory) {
            this.userDetailsFactory = userDetailsFactory;
            return this;
        }

        JwtAuthenticationFilterBuilder withAuthenticationContextFactory(AuthenticationContextFactory<UserDetails> authenticationContextFactory) {
            this.authenticationContextFactory = authenticationContextFactory;
            return this;
        }

        JwtAuthenticationFilter build() {
            return new JwtAuthenticationFilter(
                    httpRequestMatcher,
                    jwtValidator,
                    jwtParser,
                    userDetailsValidator,
                    userDetailsFactory,
                    authenticationContextFactory
            );
        }

    }

}
