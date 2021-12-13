package com.csds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProviderControlPanelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderControlPanelApplication.class, args);
	}

}
