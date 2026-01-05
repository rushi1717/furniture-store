package com.furniture.gateway.filter;

import com.furniture.gateway.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
@Slf4j
@Component
public class AuthenticationFilter
        extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtUtil jwtUtil;

    public AuthenticationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            // Public routes
            String path = request.getURI().getPath();
            if (path.startsWith("/api/v1/auth")) {
                return chain.filter(exchange);
            }

            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            if (route == null) {
                return onError(exchange, "Route not found", HttpStatus.NOT_FOUND);
            }

            Object rolesMeta = route.getMetadata().get("roles");
            List<String> allowedRoles =
                    rolesMeta == null
                            ? List.of()
                            : Arrays.stream(rolesMeta.toString().split(","))
                            .map(String::trim)
                            .map(String::toUpperCase)
                            .toList();

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            if (!jwtUtil.validateToken(token)) {
                return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
            }

            String userId = jwtUtil.extractUserId(token);
            String role = jwtUtil.extractRole(token);
            if (role == null || role.isBlank()) {
                return onError(exchange, "Role missing in token", HttpStatus.FORBIDDEN);
            }
            String roleUpper = role.toUpperCase();


            if (!allowedRoles.isEmpty() && !allowedRoles.contains(roleUpper)) {
                return onError(exchange, "Forbidden"+" "+roleUpper, HttpStatus.FORBIDDEN);
            }

            ServerHttpRequest mutatedRequest = request.mutate()
                    .headers(h -> {
                        h.remove("X-User-Id");
                        h.remove("X-User-Role");
                    })
                    .header("X-User-Id", userId)
                    .header("X-User-Role", roleUpper)
                    .build();
            log.info("Authenticated request - userId={}, role={}, path={}",
                    userId, roleUpper, request.getURI().getPath());

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.writeWith(
                Mono.just(response.bufferFactory()
                        .wrap(err.getBytes(StandardCharsets.UTF_8))));
    }

    public static class Config {}
}
