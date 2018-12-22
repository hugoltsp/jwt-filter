package com.hugoltsp.spring.boot.starter.jwt.filter.util;

import com.hugoltsp.spring.boot.starter.jwt.filter.exception.MalformedAuthorizationHeaderException;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public final class AuthorizationHeaderUtil {

    private AuthorizationHeaderUtil() {

    }

    public static String extractToken(HttpServletRequest request) {

        String authorizationHeader = getHeaderValue(request);

        if (!isHeaderValid(authorizationHeader)) {
            throw new MalformedAuthorizationHeaderException("No valid Authorization Header found");
        }

        return parseToken(authorizationHeader)
                .orElseThrow(() -> new MalformedAuthorizationHeaderException("Malformed token"));
    }

    private static String getHeaderValue(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    private static boolean isHeaderValid(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    private static Optional<String> parseToken(String authorizationHeader) {

        String token = null;

        String[] splitHeader = authorizationHeader.split(" ");

        if (splitHeader.length == 2) {
            token = splitHeader[1];
        }

        return Optional.ofNullable(token);
    }

}
