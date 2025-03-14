package com.ss.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(@NonNull CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("*").allowedHeaders("*").allowCredentials(true).allowedOrigins(
				"https://localhost", "http://localhost", "https://localhost:4200", "http://localhost:4200",
				"https://127.0.0.1", "http://127.0.0.1", "http://127.0.0.1:4200");
	}
}
