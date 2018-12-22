package com.hugoltsp.spring.boot.starter.jwt.filter.userdetails;

import io.jsonwebtoken.Claims;

import java.io.Serializable;

public interface UserDetails extends Serializable {

    Claims getClaims();

}
