/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.anonymizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main Anonymized Application
 * TODO make sure openapi description is correct, referrer-header should give us a hint.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"net.catenax.semantics.anonymizer", "org.openapitools.configuration"})
//@ImportResource({"classpath*:saynomore.lmx"})
public class Application {

	/**
	 * enable ubiquitous CORS when accessed locally
	 * @return a webmvc configurer which enables CORS
	 */
	@Bean
	public WebMvcConfigurer configurer() {
		return new WebMvcConfigurer(){
			@Override
			public void addCorsMappings(CorsRegistry registry) {
			  registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
			}			  
		};
	 }

	/**
	 * entry point if started as an app
	 * @param args command line
	 */
	public static void main(String[] args) {
		new SpringApplication(Application.class).run(args);
	}

}
