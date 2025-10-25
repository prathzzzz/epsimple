package com.eps.module;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@SpringBootApplication
@EntityScan(basePackages = {"com.eps.module"})
@EnableJpaRepositories(basePackages = {"com.eps.module"})
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class EPSOneApplication {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        log.info("===========================================");
        log.info("Starting EPSOne Application...");
        log.info("Log directory: ./logs");
        log.info("===========================================");
        SpringApplication.run(EPSOneApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String[] activeProfiles = environment.getActiveProfiles();
        String profile = activeProfiles.length > 0 ? activeProfiles[0] : "default";
        String port = environment.getProperty("server.port");

        log.info("===========================================");
        log.info("EPSOne Application Started Successfully!");
        log.info("Active Profile: {}", profile);
        log.info("Server Port: {}", port);
        log.info("Application URL: http://localhost:{}", port);
        log.info("===========================================");
    }

    @PreDestroy
    public void onShutdown() {
        log.info("===========================================");
        log.info("EPSOne Application Shutting Down...");
        log.info("===========================================");
    }

}
