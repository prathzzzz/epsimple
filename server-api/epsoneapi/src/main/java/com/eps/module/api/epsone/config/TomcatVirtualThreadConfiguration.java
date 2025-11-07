package com.eps.module.api.epsone.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * Configuration to enable Virtual Threads for Tomcat HTTP request handling
 * This significantly improves performance for I/O-bound operations
 */
@Slf4j
@Configuration
public class TomcatVirtualThreadConfiguration {
    
    /**
     * Configure Tomcat to use virtual threads for handling HTTP requests
     * Only enabled when virtual threads are available (Java 21+)
     */
    @Bean
    @ConditionalOnProperty(
        name = "spring.threads.virtual.enabled",
        havingValue = "true",
        matchIfMissing = false
    )
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        log.info("Configuring Tomcat to use Virtual Threads for HTTP request handling");
        
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
            log.info("Tomcat configured with Virtual Thread Executor");
        };
    }
}
