package com.example.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil {
    private static final String ROLES_CLAIM = "roles";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    private Key SECRET_KEY;

    @PostConstruct
    public void init() {
        // Use environment variable if available, otherwise generate
        String envKey = System.getenv("JWT_SECRET");
        if (envKey != null && !envKey.isEmpty()) {
            SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(envKey));
        } else {
            SECRET_KEY = Keys.secretKeyFor(SIGNATURE_ALGORITHM);
        }
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        return Jwts.builder()
                .setSubject(username)
                .claim(ROLES_CLAIM, authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SIGNATURE_ALGORITHM)
                .compact();
    }

    public String refreshToken(String token) {
        Claims claims = parseToken(token).getBody();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SIGNATURE_ALGORITHM)
                .compact();
    }

    public String extractUsername(String token) {
        return parseToken(token).getBody().getSubject();
    }

    public List<String> extractRoles(String token) {
        return parseToken(token).getBody().get(ROLES_CLAIM, List.class);
    }

    public boolean isTokenValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error(ex.getMessage());
            // Token expired but signature valid
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return parseToken(token).getBody().getExpiration().before(new Date());
        } catch (JwtException ex) {
            return true;
        }
    }

    public long getRemainingValidity(String token) {
        Date expiration = parseToken(token).getBody().getExpiration();
        return (expiration.getTime() - System.currentTimeMillis()) / 1000;
    }

    private Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
    }

    public long getExpirationTime() {
        return EXPIRATION_TIME / 1000;
    }
}