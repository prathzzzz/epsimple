package com.eps.module.api.epsone.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Configuration for Virtual Threads (Java 21+) and Async Processing
 * Virtual threads provide lightweight concurrency for better performance
 */
@Slf4j
@Configuration
@EnableAsync
public class VirtualThreadConfiguration implements AsyncConfigurer {
    
    /**
     * Configure async executor to use virtual threads with security context propagation
     * Virtual threads are perfect for I/O-bound operations like bulk uploads
     * DelegatingSecurityContextAsyncTaskExecutor ensures SecurityContext is propagated to async threads
     */
    @Bean(name = "taskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        log.info("Configuring Virtual Thread Executor for async operations with security context propagation");
        Executor virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();
        TaskExecutorAdapter taskExecutorAdapter = new TaskExecutorAdapter(virtualThreadExecutor);
        return new DelegatingSecurityContextAsyncTaskExecutor(taskExecutorAdapter);
    }
    
    /**
     * Handle uncaught exceptions in async methods
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            log.error("[ASYNC ERROR] Exception in async method: {} with params: {}", 
                method.getName(), params, throwable);
        };
    }
}
