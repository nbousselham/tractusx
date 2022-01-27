package com.csds.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("core-service", r -> r.path("/core/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://core-service"))
                
                .route("provider-service", r -> r.path("/provider/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://provider-service"))
                
                .route("documentation-service", r -> r.path("/documentation/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://documentation-service"))
                
                .route("consumer-service", r -> r.path("/consumer/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://consumer-service"))
                
                .build();
    }

}