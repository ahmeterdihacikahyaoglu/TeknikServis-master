package com.garanti.TeknikServis.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class TokenParser {
    private static final String SECRET_KEY = "MY_SECRET_KEY";

    public static String jwt(String token) {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
            return claims.getSubject().split("-")[0];
    }
}
