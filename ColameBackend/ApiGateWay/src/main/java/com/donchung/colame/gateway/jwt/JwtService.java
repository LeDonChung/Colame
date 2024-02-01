package com.donchung.colame.gateway.jwt;

import io.jsonwebtoken.Claims;

import java.security.Key;

public interface JwtService {

    Claims extractClaims(String token);

    Key getKey();

    boolean isValidToken(String token);
}
