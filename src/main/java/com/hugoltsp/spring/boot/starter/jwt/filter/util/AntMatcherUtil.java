package com.hugoltsp.spring.boot.starter.jwt.filter.util;

import org.springframework.util.AntPathMatcher;

public final class AntMatcherUtil {

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private AntMatcherUtil() {

    }

    public static boolean matches(String pattern1, String pattern2) {

        return ANT_PATH_MATCHER.match(pattern1, pattern2);
    }

}
