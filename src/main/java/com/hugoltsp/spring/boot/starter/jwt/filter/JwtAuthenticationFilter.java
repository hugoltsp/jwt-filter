package com.hugoltsp.spring.boot.starter.jwt.filter;

import com.hugoltsp.spring.boot.starter.jwt.filter.authentication.AuthenticationContextFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.authentication.AuthenticationContextHolder;
import com.hugoltsp.spring.boot.starter.jwt.filter.parser.JwtParser;
import com.hugoltsp.spring.boot.starter.jwt.filter.request.HttpRequest;
import com.hugoltsp.spring.boot.starter.jwt.filter.request.RequestMatcher;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetails;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsValidator;
import com.hugoltsp.spring.boot.starter.jwt.filter.util.AuthorizationHeaderUtil;
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

    private final RequestMatcher requestMatcher;

    private final JwtParser jwtParser;

    private final UserDetailsValidator<UserDetails> userDetailsValidator;

    private final UserDetailsFactory<UserDetails> userDetailsFactory;

    private final AuthenticationContextFactory<UserDetails> authenticationContextFactory;

    public JwtAuthenticationFilter(RequestMatcher requestMatcher,
                                   JwtParser jwtParser,
                                   UserDetailsValidator<UserDetails> userDetailsValidator,
                                   UserDetailsFactory<UserDetails> userDetailsFactory,
                                   AuthenticationContextFactory<UserDetails> authenticationContextFactory) {
        this.requestMatcher = requestMatcher;
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

        if (requestMatcher.isPublic(httpRequest)) {

            httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        } else {

            try {

                Claims claims = jwtParser.parse(AuthorizationHeaderUtil.extractToken(httpRequest));

                Optional<UserDetails> userDetails = userDetailsFactory.createByClaims(claims);

                userDetails.ifPresent(userDetailsValidator::validate);

                AuthenticationContextHolder.set(authenticationContextFactory.create(userDetails));

            } catch (Exception e) {
                LOGGER.error("Invalid token.", e);
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
