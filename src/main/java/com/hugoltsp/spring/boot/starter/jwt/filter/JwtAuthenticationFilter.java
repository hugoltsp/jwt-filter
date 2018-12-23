package com.hugoltsp.spring.boot.starter.jwt.filter;

import com.hugoltsp.spring.boot.starter.jwt.filter.authentication.AuthenticationContextFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.authentication.AuthenticationContextHolder;
import com.hugoltsp.spring.boot.starter.jwt.filter.setting.JwtAuthenticationSettings;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetails;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsValidator;
import com.hugoltsp.spring.boot.starter.jwt.filter.util.AuthorizationHeaderUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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

    private final JwtAuthenticationSettings settings;

    private final UserDetailsValidator<UserDetails> userDetailsValidator;

    private final UserDetailsFactory<UserDetails> userDetailsFactory;

    private final AuthenticationContextFactory<UserDetails> authenticationContextFactory;

    public JwtAuthenticationFilter(JwtAuthenticationSettings settings,
                                   UserDetailsValidator userDetailsValidator,
                                   UserDetailsFactory userDetailsFactory,
                                   AuthenticationContextFactory authenticationContextFactory) {
        this.settings = settings;
        this.userDetailsValidator = userDetailsValidator;
        this.userDetailsFactory = userDetailsFactory;
        this.authenticationContextFactory = authenticationContextFactory;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        if (settings.isPublic(request)) {

            response.setStatus(HttpServletResponse.SC_OK);

        } else {

            try {

                Claims claims = Jwts.parser()
                        .setSigningKey(settings.getSecretKey())
                        .parseClaimsJws(AuthorizationHeaderUtil.extractToken(request))
                        .getBody();

                Optional<UserDetails> userDetails = userDetailsFactory.createByClaims(claims);

                userDetails.ifPresent(user -> userDetailsValidator.validate(user));

                AuthenticationContextHolder.set(authenticationContextFactory.create(userDetails));

            } catch (Exception e) {
                LOGGER.error("Invalid token.", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }

        }

        filterChain.doFilter(request, response);
    }

}
