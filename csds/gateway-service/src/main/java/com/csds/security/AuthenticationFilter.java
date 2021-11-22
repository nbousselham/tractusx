package com.csds.security;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

	@Autowired
	private RouterValidator routerValidator;

	@Autowired
	private ServiceInitiator serviceInitiator;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String path = request.getURI().getPath();
		String serviceName = "";
		if (path != null) {
			serviceName = path.split("/")[1];
		}
		//Disable token filteration we can enable it later
		if (routerValidator.isSecured.test(request)) {
			try {
				if (serviceInitiator.isInternalServiceIntiator(request)) {
				} else {
					if (this.isAuthMissing(request))
						return this.onError(exchange, "Authorization header is missing in request",
								HttpStatus.UNAUTHORIZED);

					final String token = this.getAuthHeader(request);

					this.populateRequestWithHeaders(exchange, token, serviceName);

					if (jwtUtil.isInvalid(token))
						return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
				}
			} catch (Exception e) {
				return this.onError(exchange, "Unable to recognized Authorization header", HttpStatus.UNAUTHORIZED);
			}
		}
		return chain.filter(exchange);
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
		return exchange.getResponse().writeWith(Flux.just(buffer));
		// return response.setComplete();
	}

	private String getAuthHeader(ServerHttpRequest request) {
		return request.getHeaders().getOrEmpty("Authorization").get(0);
	}

	private boolean isAuthMissing(ServerHttpRequest request) {
		return !request.getHeaders().containsKey("Authorization");
	}

	private void populateRequestWithHeaders(ServerWebExchange exchange, String token, String serviceName) {
		Claims claims = jwtUtil.getAllClaimsFromToken(token);
		String serviceValue = serviceInitiator.getUUIDforService(serviceName);
		exchange.getRequest().mutate().header("serviceName", serviceName).header("serviceValue", serviceValue)
				.header("id", String.valueOf(claims.get("id"))).header("role", String.valueOf(claims.get("role")))
				.header("fullname", String.valueOf(claims.get("fullname"))).build();
	}
}