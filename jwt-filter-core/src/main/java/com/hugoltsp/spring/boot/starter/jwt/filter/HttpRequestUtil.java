package com.hugoltsp.spring.boot.starter.jwt.filter;

import java.util.Optional;

final class HttpRequestUtil {

    private HttpRequestUtil() {

    }

    static String extractToken(HttpRequest httpRequest) {

        return Optional.ofNullable(httpRequest.getAuthorizationHeader())
                .filter(HttpRequestUtil::isHeaderValid)
                .map(HttpRequestUtil::parseToken)
                .orElseThrow(() -> new JwtAuthenticationFilterException(String.format("Malformed token [%s], No valid Authorization Header found", httpRequest.getAuthorizationHeader())));
    }

    private static boolean isHeaderValid(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    private static String parseToken(String authorizationHeader) {

        String token = null;

        String[] splitHeader = authorizationHeader.split(" ");

        if (splitHeader.length == 2) {
            token = splitHeader[1];
        }

        return token;
    }

}
