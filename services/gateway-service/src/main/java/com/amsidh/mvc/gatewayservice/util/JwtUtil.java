package com.amsidh.mvc.gatewayservice.util;

import com.amsidh.mvc.gatewayservice.config.JwtConfigEnum;
import com.amsidh.mvc.gatewayservice.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for JWT token operations.
 * <p>
 * Handles JWT token generation, validation, and claims extraction.
 * Uses HS256 (HMAC with SHA-256) algorithm for token signing.
 * <p>
 * Token Structure:
 * - Header: Algorithm and token type
 * - Payload: Claims (subject/email, issued date, expiration)
 * - Signature: HMAC SHA-256 signature
 * <p>
 * Security Notes:
 * - Secret key must be at least 256 bits (32 bytes) for HS256
 * - Tokens are stateless - no server-side session storage
 * - Token validation checks signature, expiration, and subject
 *
 * @author Amsidh Mohammed
 */
@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    /**
     * Validate the JWT token.
     * <p>
     * Validation checks:
     * 1. Email in token matches the provided email
     * 2. Token has not expired
     * 3. Token signature is valid
     *
     * @param token the JWT token to validate
     * @param email the expected email address
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token, String email) {
        log.info("Validating token for email: {}", email);

        try {
            final String tokenEmail = extractEmail(token);
            boolean isValid = tokenEmail.equals(email) && !isTokenExpired(token);

            if (isValid) {
                log.info("Token validation successful for email: {}", email);
            } else {
                log.warn("Token validation failed for email: {} - Email match: {}, Expired: {}",
                        email, tokenEmail.equals(email), isTokenExpired(token));
            }

            return isValid;
        } catch (AuthenticationException e) {
            log.error("Token validation failed for email: {} - Error: {}", email, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during token validation for email: {} - Error: {}",
                    email, e.getMessage(), e);
            throw new AuthenticationException("Token validation failed", e);
        }
    }

    /**
     * Extract the email (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return the email address stored in the token's subject claim
     * @throws AuthenticationException if token is invalid or expired
     */
    public String extractEmail(String token) {
        log.debug("Extracting email from token");
        try {
            String email = extractClaim(token, Claims::getSubject);
            log.debug("Extracted email: {}", email);
            return email;
        } catch (ExpiredJwtException e) {
            log.error("Token has expired: {}", e.getMessage());
            throw new AuthenticationException("Token has expired", e);
        } catch (JwtException e) {
            log.error("Invalid token: {}", e.getMessage());
            throw new AuthenticationException("Invalid token", e);
        }
    }

    /**
     * Extract the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract a specific claim from the JWT token.
     *
     * @param token          the JWT token
     * @param claimsResolver function to extract the desired claim
     * @param <T>            the type of the claim
     * @return the extracted claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from the JWT token.
     *
     * @param token the JWT token
     * @return all claims contained in the token
     * @throws JwtException if token is invalid or expired
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Check if the JWT token has expired.
     *
     * @param token the JWT token
     * @return true if token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            boolean expired = expiration.before(new Date());
            log.debug("Token expiration check - Expires: {}, Expired: {}", expiration, expired);
            return expired;
        } catch (ExpiredJwtException e) {
            log.debug("Token is expired");
            return true;
        }
    }


    /**
     * Get the signing key for JWT token operations.
     * <p>
     * Converts the SECRET_KEY string to a SecretKey object.
     * Key must be at least 256 bits (32 bytes) for HS256 algorithm.
     *
     * @return the signing key
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = JwtConfigEnum.INSTANCE.getJwtSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
