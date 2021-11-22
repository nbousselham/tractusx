package com.csds.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class ServiceInitiator {

	public static final Map<String, String> openServiceIntiator = new HashMap<>();
	static {
		UUID uuid = UUID.randomUUID();
		openServiceIntiator.put("core", uuid.toString());
		uuid = UUID.randomUUID();
		openServiceIntiator.put("provider", uuid.toString());

	}

	public boolean isInternalServiceIntiator(ServerHttpRequest request) {
		try {
			String serviceName = request.getHeaders().getOrEmpty("serviceName").get(0);
			String serviceValue = request.getHeaders().getOrEmpty("serviceValue").get(0);
			String foundserviceNameVal = openServiceIntiator.get(serviceName);
			if (Optional.ofNullable(foundserviceNameVal).isEmpty()) {
				return false;
			}
			return foundserviceNameVal.equals(serviceValue);
		} catch (Exception e) {
		}
		return false;
	}

	public String getUUIDforService(String serviceName) {
		return openServiceIntiator.get(serviceName);
	}

}
