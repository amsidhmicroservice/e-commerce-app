package com.amsidh.mvc.gatewayservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Global filter for logging all requests and responses passing through the
 * gateway.
 * Captures request details, response status, and execution time for tracing
 * purposes.
 * Uses Reactor Context for traceId propagation in reactive environment.
 */
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String START_TIME = "startTime";

    /**
     * Filters incoming requests and outgoing responses.
     * Logs comprehensive request/response details including headers, status codes,
     * and timing.
     * 
     * @param exchange the current server exchange
     * @param chain    the filter chain
     * @return Mono indicating completion of the filter
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Capture start time for duration calculation
        long startTime = System.currentTimeMillis();
        exchange.getAttributes().put(START_TIME, startTime);

        // Get or generate traceId - check headers first, then generate
        String traceId = request.getHeaders().getFirst("X-B3-TraceId");
        if (traceId == null || traceId.isEmpty()) {
            traceId = java.util.UUID.randomUUID().toString();
        }

        // Add traceId to request headers for downstream services
        final String finalTraceId = traceId;
        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-B3-TraceId", finalTraceId)
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

        // Log incoming request details
        logRequest(mutatedRequest, finalTraceId);

        // Continue with the filter chain and log response details
        return chain.filter(mutatedExchange).then(
                Mono.fromRunnable(() -> {
                    ServerHttpResponse response = mutatedExchange.getResponse();
                    Long start = mutatedExchange.getAttribute(START_TIME);
                    long duration = start != null ? System.currentTimeMillis() - start : 0;
                    logResponse(mutatedRequest, response, finalTraceId, duration);
                }));
    }

    /**
     * Logs detailed information about the incoming request.
     * 
     * @param request the server HTTP request
     * @param traceId the trace identifier for request tracking
     */
    private void logRequest(ServerHttpRequest request, String traceId) {
        log.info("=== Incoming Request === TraceId: {} | Method: {} | URI: {} | Remote Address: {}",
                traceId,
                request.getMethod(),
                request.getURI(),
                request.getRemoteAddress());

        // Log important headers
        HttpHeaders headers = request.getHeaders();
        String userAgent = headers.getFirst(HttpHeaders.USER_AGENT);
        log.debug("Request Headers - TraceId: {} | Content-Type: {} | Accept: {} | User-Agent: {}",
                traceId,
                headers.getContentType(),
                headers.getAccept(),
                userAgent != null ? userAgent : "N/A");

        // Log query parameters if present
        if (!request.getQueryParams().isEmpty()) {
            log.debug("Query Parameters - TraceId: {} | Params: {}", traceId, request.getQueryParams());
        }
    }

    /**
     * Logs detailed information about the outgoing response.
     * 
     * @param request  the server HTTP request
     * @param response the server HTTP response
     * @param traceId  the trace identifier for request tracking
     * @param duration the request processing duration in milliseconds
     */
    private void logResponse(ServerHttpRequest request, ServerHttpResponse response, String traceId, long duration) {
        log.info("=== Outgoing Response === TraceId: {} | Method: {} | URI: {} | Status: {} | Duration: {}ms",
                traceId,
                request.getMethod(),
                request.getURI(),
                response.getStatusCode(),
                duration);

        // Log response headers
        HttpHeaders headers = response.getHeaders();
        log.debug("Response Headers - TraceId: {} | Content-Type: {} | Content-Length: {}",
                traceId,
                headers.getContentType(),
                headers.getContentLength());

        // Log warning for slow requests (> 1 second)
        if (duration > 1000) {
            log.warn("Slow request detected - TraceId: {} | URI: {} | Duration: {}ms",
                    traceId, request.getURI(), duration);
        }
    }

    /**
     * Defines the order of this filter in the filter chain.
     * Lower values have higher priority.
     * 
     * @return the order value (highest priority)
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
