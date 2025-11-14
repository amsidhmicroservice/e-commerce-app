package com.amsidh.mvc.authservice.util;

import com.amsidh.mvc.authservice.config.JwtConfigEnum;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {


    public String generateToken(String email) {
        log.info("Generating JWT token for email: {}", email);

        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims, email);

        log.debug("JWT token generated successfully for email: {}", email);
        return token;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + JwtConfigEnum.INSTANCE.getJwtExpiration());

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

    private SecretKey getSigningKey() {
        byte[] keyBytes = JwtConfigEnum.INSTANCE.getJwtSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
