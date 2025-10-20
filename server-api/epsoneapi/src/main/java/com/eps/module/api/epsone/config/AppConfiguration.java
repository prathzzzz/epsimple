package com.eps.module.api.epsone.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import jakarta.annotation.PostConstruct;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableJpaAuditing
public class AppConfiguration {
  
	/*
	 * This is a bootstraping class which enables the 
	 * spring managed features in the project integrated 
	 * with multiple modules used in the project.
	 * 
	 */
	
	@PostConstruct
	public void init() {
		log.info("AppConfiguration initialized - WebSecurity, MethodSecurity, and JpaAuditing enabled");
	}
}





