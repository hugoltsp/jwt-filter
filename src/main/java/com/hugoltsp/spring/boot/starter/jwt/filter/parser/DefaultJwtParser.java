package com.hugoltsp.spring.boot.starter.jwt.filter.parser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class DefaultJwtParser implements JwtParser {

    private final String secretKey;

    public DefaultJwtParser(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public Claims parse(String token) {

        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

    }

}
