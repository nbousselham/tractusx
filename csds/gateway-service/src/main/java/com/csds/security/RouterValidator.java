package com.csds.security;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouterValidator {

	public static final List<String> openApiEndpoints = List.of("/auth/register", "/auth/login","swagger-ui.html");

	public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints.stream()
			.noneMatch(uri -> {
				return true;
//				return request.getURI().getPath().contains(uri);
				});

	public boolean getUserRoleAccess(String requestURL) {
		return true;
	}

}