package com.amsidh.mvc.gatewayservice.filter;

import com.amsidh.mvc.gatewayservice.exception.AuthenticationException;
import com.amsidh.mvc.gatewayservice.util.JwtUtil;
import com.amsidh.mvc.gatewayservice.validator.RouteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final JwtUtil jwtUtil;

    public AuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public int getOrder() {
        return 1; // Execute after LoggingFilter
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        log.debug("Authentication filter processing request: {}", path);
        // Skip authentication for public endpoints
        final boolean isSecured = RouteValidator.isSecured.test(request);
        if (isSecured) {
            // Check if the Authorization header exists and starts with "Bearer "
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION) ||
                    !request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION).startsWith("Bearer ")) {
                log.error("Missing or invalid authorization header for path: {}", path);
                return onError(exchange, "Missing or invalid authorization header", HttpStatus.UNAUTHORIZED);
            }

            // Validate token with auth-service
            return validateTokenWithAuthService(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                    .flatMap(email -> {
                        // Add user email to request headers for downstream services
                        ServerHttpRequest mutatedRequest = request.mutate()
                                .header("X-User-Email", email)
                                .build();
                        // Continue with modified request
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    });
        }

        return chain.filter(exchange);
    }


    private Mono<String> validateTokenWithAuthService(String bearerToken) {
        final String jwtToken = bearerToken.substring(7);
        try {
            // Extract email from token
            String email = jwtUtil.extractEmail(jwtToken);
            log.debug("Extracted email from token: {}", email);

            // Validate token (signature + expiration + subject)
            final boolean isValidToken = jwtUtil.validateToken(jwtToken, email);
            if (isValidToken) {
                log.debug("Token validated successfully for email: {}", email);
                return Mono.justOrEmpty(email);
            } else {
                log.error("Invalid token");
                return Mono.empty();
            }
        } catch (AuthenticationException e) {
            log.error("Token validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during token validation", e);
            throw new AuthenticationException("Token validation failed: " + e.getMessage());
        }
    }

    private Mono<Void> handleAuthError(ServerWebExchange exchange, WebClientResponseException ex) {
        if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }
        if (ex.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            return onError(exchange, "Authentication service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
        return onError(exchange, "Authentication service error", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String body = String.format("{\"status\": %d, \"error\": \"%s\"}",
                httpStatus.value(), err);

        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
