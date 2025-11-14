package com.amsidh.mvc.authservice.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public enum JwtConfigEnum {
    INSTANCE;

    private volatile String jwtSecret;
    private volatile Long jwtExpiration;
    private volatile boolean initialized = false;

    @Component
    public static class Config {

        @Value("${jwt.secret}")
        private String secret;

        @Value("${jwt.expiration:86400000}")
        private Long expiration;

        @PostConstruct
        public void initializeEnum() {
            if (secret == null || secret.trim().isEmpty()) {
                throw new IllegalStateException("jwt.secret property is required");
            }

            if (expiration == null || expiration <= 0) {
                throw new IllegalStateException("jwt.expiration must be positive");
            }

            INSTANCE.jwtSecret = secret;
            INSTANCE.jwtExpiration = expiration;
            INSTANCE.initialized = true;
        }
    }

    public String getJwtSecret() {
        checkInitialized();
        return jwtSecret;
    }

    public Long getJwtExpiration() {
        checkInitialized();
        return jwtExpiration;
    }

    public boolean isInitialized() {
        return initialized;
    }

    private void checkInitialized() {
        if (!initialized) {
            throw new IllegalStateException("JwtConfigEnum not initialized");
        }
    }
}