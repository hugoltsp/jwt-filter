package com.hugoltsp.spring.boot.starter.jwt.filter.util;

import com.hugoltsp.spring.boot.starter.jwt.filter.exception.MalformedAuthorizationHeaderException;
import com.hugoltsp.spring.boot.starter.jwt.filter.request.HttpRequest;

import java.util.Optional;

public final class AuthorizationHeaderUtil {

    private AuthorizationHeaderUtil() {

    }

    public static String extractToken(HttpRequest httpRequest) {

        String authorizationHeader = httpRequest.getAuthorizationHeader();

        if (!isHeaderValid(authorizationHeader)) {
            throw new MalformedAuthorizationHeaderException("No valid Authorization Header found");
        }

        return parseToken(authorizationHeader)
                .orElseThrow(() -> new MalformedAuthorizationHeaderException("Malformed token"));
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
