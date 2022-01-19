package com.csds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ConsumerControlPanelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerControlPanelApplication.class, args);
	}

}
