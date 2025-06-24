package com.craig.digital_book_store.security.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.craig.digital_book_store.service.UserDetailsImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private Key key;

        public String generateJwtToken(Authentication authentication) {

            UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

            if (key == null) {
                byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
                key = Keys.hmacShaKeyFor(keyBytes);
            }

            return Jwts.builder()
                    .setSubject((userPrincipal.getUsername()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
        }
    public String getUserNameFromJwtToken(String token) {
        if (key == null) {
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
            key = Keys.hmacShaKeyFor(keyBytes);
        }

        return Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            if (key == null) {
                byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
                key = Keys.hmacShaKeyFor(keyBytes);
            }
        
        Jwts.parserBuilder().setSigningKey(key).build().parse(authToken);
        return true;
        } catch (JwtException e) {
            logger.error("JWT token validation error: {}", e.getMessage());
        }
        return false;
    }

    public String generateTokenFromUsername(String username) {
    if (key == null) {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        key = Keys.hmacShaKeyFor(keyBytes);
    }
    
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
}
}
