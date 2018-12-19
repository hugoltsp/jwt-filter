package com.hugoltsp.spring.boot.starter.jwt.filter.util;

import com.hugoltsp.spring.boot.starter.jwt.filter.exception.MalformedAuthorizationHeaderException;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class JwtTokenUtil {

    private JwtTokenUtil() {

    }

    public static String extractTokenFromRequest(HttpServletRequest request) {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!isHeaderValid(authorizationHeader)) {
            throw new MalformedAuthorizationHeaderException("No valid Authorization Header found");
        }

        return getTokenFromHeader(authorizationHeader)
                .orElseThrow(() -> new MalformedAuthorizationHeaderException("Malformed token"));
    }

    private static boolean isHeaderValid(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    private static Optional<String> getTokenFromHeader(String authorizationHeader) {
        return Optional.of(parseToken(authorizationHeader));
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
