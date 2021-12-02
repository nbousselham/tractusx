package com.csds.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import feign.auth.BasicAuthRequestInterceptor;
import okhttp3.OkHttpClient;

@Configuration
public class DataspaceConnectorConfiguration {

	@Value("${connector.username}")
	private String username;

	@Value("${connector.password}")
	private String password;

	@Bean
	public BasicAuthRequestInterceptor adminAuth() {
		return new BasicAuthRequestInterceptor(username, password);
	}

	@Bean
	@Primary
	public OkHttpClient client() {
		return new OkHttpClient().newBuilder().hostnameVerifier((s, sslSession) -> {
			return true;
		}).build();
	}

}
