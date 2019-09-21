package com.hugoltsp.spring.boot.starter.jwt.filter;

import org.springframework.util.AntPathMatcher;

final class AntMatcherUtil {

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private AntMatcherUtil() {

    }

    static boolean matches(String pattern1, String pattern2) {

        return ANT_PATH_MATCHER.match(pattern1, pattern2);
    }

}
