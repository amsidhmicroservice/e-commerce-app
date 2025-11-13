package com.amsidh.mvc.gatewayservice.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    // List of public endpoints that don't require authentication
    private static final List<String> openApiEndpoints = List.of(
            "/api/v1/auth-service/auth/register",
            "/api/v1/auth-service/auth/token",
            "/api/v1/auth-service/auth/validate",
            "/actuator/",
            "/auth/login",
            "/auth/validate",
            "/auth/forgot-password",
            "/auth/reset-password",
            "/eureka/**",
            "/actuator/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/swagger-resources/**"
    );

    public final Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

    // You can also add method to check if a path is public
    public boolean isPublicEndpoint(String path) {
        return openApiEndpoints.stream()
                .anyMatch(uri -> path.contains(uri));
    }

    // Optional: Add method to check specific role-based access
    public boolean hasRequiredRole(ServerHttpRequest request, String requiredRole) {
        String roles = request.getHeaders().getFirst("X-User-Roles");
        if (roles != null) {
            return Arrays.asList(roles.split(",")).contains(requiredRole);
        }
        return false;
    }
}

