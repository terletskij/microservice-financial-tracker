package com.financialtracker.auth_service.security.jwt;

import com.financialtracker.auth_service.dto.response.JwtAuthenticationResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtService {
    // TODO: configure .env file with clear naming and without duplicates
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int expirationTime;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    public JwtAuthenticationResponse generateAuthToken(String email, Long userId) {
        JwtAuthenticationResponse jwtDto = new JwtAuthenticationResponse();
        jwtDto.setToken(generateJwtToken(email, userId));
        jwtDto.setRefreshToken(generateRefreshToken(email, userId));
        return jwtDto;
    }

    public JwtAuthenticationResponse refreshBaseToken(String email, Long userId, String refreshToken) {
        JwtAuthenticationResponse jwtDto = new JwtAuthenticationResponse();
        jwtDto.setToken(generateJwtToken(email, userId));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    public boolean validateJwt(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn(e.getMessage());
        }
        return false;
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    private String generateJwtToken(String email, Long userId) {
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId.toString())
                .expiration(expirationDate)
                .signWith(getSignKey())
                .compact();
    }

    private String generateRefreshToken(String email, Long userId) {
        Date expirationDate = new Date(System.currentTimeMillis() + refreshTokenExpiration);
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId.toString())
                .expiration(expirationDate)
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

}