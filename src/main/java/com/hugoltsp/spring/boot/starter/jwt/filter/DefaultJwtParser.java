package com.hugoltsp.spring.boot.starter.jwt.filter;

import com.hugoltsp.spring.boot.starter.jwt.filter.parser.JwtParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

class DefaultJwtParser implements JwtParser {

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
