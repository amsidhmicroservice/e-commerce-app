package com.amsidh.mvc.authservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for JWT token operations.
 * 
 * Handles JWT token generation, validation, and claims extraction.
 * Uses HS256 (HMAC with SHA-256) algorithm for token signing.
 * 
 * Token Structure:
 * - Header: Algorithm and token type
 * - Payload: Claims (subject/email, issued date, expiration)
 * - Signature: HMAC SHA-256 signature
 * 
 * Security Notes:
 * - Secret key must be at least 256 bits (32 bytes) for HS256
 * - Tokens are stateless - no server-side session storage
 * - Token validation checks signature, expiration, and subject
 * 
 * @author Amsidh Mohammed
 */
@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private Long JWT_EXPIRATION;

    /**
     * Generate a JWT token for the given email.
     * 
     * Token contains:
     * - Subject: User's email
     * - Issued At: Current timestamp
     * - Expiration: Current time + JWT_EXPIRATION
     * - Signature: HS256 with secret key
     * 
     * @param email the user's email address (used as subject)
     * @return JWT token string
     */
    public String generateToken(String email) {
        log.info("Generating JWT token for email: {}", email);

        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims, email);

        log.debug("JWT token generated successfully for email: {}", email);
        return token;
    }

    /**
     * Create a JWT token with specified claims and subject.
     * 
     * @param claims  additional claims to include in the token
     * @param subject the subject (user email) of the token
     * @return JWT token string
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + JWT_EXPIRATION);

        log.debug("Creating token - Subject: {}, IssuedAt: {}, Expiration: {}",
                subject, now, expirationDate);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Get the signing key for JWT token operations.
     * 
     * Converts the SECRET_KEY string to a SecretKey object.
     * Key must be at least 256 bits (32 bytes) for HS256 algorithm.
     * 
     * @return the signing key
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
