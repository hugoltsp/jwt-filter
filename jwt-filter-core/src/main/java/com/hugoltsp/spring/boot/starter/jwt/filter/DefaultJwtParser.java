package com.hugoltsp.spring.boot.starter.jwt.filter;

import com.hugoltsp.spring.boot.starter.jwt.filter.token.JwtParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.util.Assert;

class DefaultJwtParser implements JwtParser {

    private final String secretKey;

    public DefaultJwtParser(String secretKey) {
        Assert.hasText(secretKey, "Illegal secret key, it cannot be blank.");
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
