package com.amsidh.mvc.gatewayservice.filter;

import com.amsidh.mvc.gatewayservice.validator.RouteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final RouteValidator routeValidator;
    private final WebClient.Builder webClientBuilder;

    @Value("${auth.service.url:lb://AUTH-SERVICE}")
    private String authServiceUrl;

    public AuthenticationFilter(RouteValidator routeValidator, WebClient.Builder webClientBuilder) {
        this.routeValidator = routeValidator;
        this.webClientBuilder = webClientBuilder;
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
        if (routeValidator.isSecured.test(request)) {
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
        log.debug("Validating token with auth-service: {}", authServiceUrl);
        // Build the complete validation endpoint URL
        String validateEndpoint = authServiceUrl + "/api/v1/auth-service/auth/validate";
        final String jwtToken = bearerToken.substring(7);
        return webClientBuilder.build()
                .get()
                .uri(validateEndpoint + "?token=" + jwtToken) // ✅ Variable IS used here
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(ex -> {
                    log.error("Error validating token with auth-service", ex);
                    return ex;
                });
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
