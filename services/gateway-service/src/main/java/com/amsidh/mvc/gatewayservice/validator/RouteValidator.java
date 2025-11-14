package com.amsidh.mvc.gatewayservice.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    private static final Logger log = LoggerFactory.getLogger(RouteValidator.class);

    // List of public endpoints that don't require authentication
    private static final List<String> openApiEndpoints = List.of(
            "/api/v1/auth-service/auth/register",
            "/api/v1/auth-service/auth/token",
            "/api/v1/auth-service/auth/validate",
            "/actuator",
            "/auth/login",
            "/auth/validate",
            "/auth/forgot-password",
            "/auth/reset-password",
            "/eureka",
            "/actuator",
            "/swagger-ui",
            "/v3/api-docs",
            "/webjars",
            "/swagger-resources");

    // Generic predicate that checks for open API endpoints
    // Generic predicate that checks for open API endpoints
    public static final Predicate<ServerHttpRequest> isSecured =
            request -> {
                final String path = request.getURI().getPath().split("#")[0].split("\\?")[0];
                log.info("Checking path: {}", path);

                // Check if a path contains any of the open API patterns
                final boolean isOpenApiEndpoint = openApiEndpoints.stream()
                        .anyMatch(path::contains);

                final boolean isSecured = !isOpenApiEndpoint;
                log.info("Path '{}' is secured: {}", path, isSecured);
                return isSecured;
            };

}

