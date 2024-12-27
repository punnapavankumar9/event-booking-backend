package com.punna.identity.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    public static final long TOKEN_VALIDITY = 1000 * 60 * 60;
    public final SecretKey SECRET_KEY;

    public JwtService(@Value("${application.security.jwt-secret-key}") String key) {
        SECRET_KEY = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateToken(String username) {
        Map<String, String> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public String createToken(Map<String, String> claims, String username) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .issuedAt(new Date())
                .signWith(SECRET_KEY, Jwts.SIG.HS256)
                .compact();
    }

    public String parseUsername(String token) {
        return parseToken(token).getSubject();
    }

    public boolean isExpired(String token) {
        return parseToken(token)
                .getExpiration()
                .before(new Date());
    }

    public Claims parseToken(String token) {
        return Jwts
                .parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
