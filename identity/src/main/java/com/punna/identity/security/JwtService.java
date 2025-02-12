package com.punna.identity.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.punna.commons.exception.EventApplicationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

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
        try {
            return parseToken(token)
                .getExpiration()
                .before(new Date());

        } catch (ExpiredJwtException e) {
            throw new EventApplicationException("Token has expired",
                HttpStatus.UNAUTHORIZED.value());
        } catch (Exception e) {
            throw new EventApplicationException("Invalid JWT token",
                HttpStatus.UNAUTHORIZED.value());
        }
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
