package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

    @Bean
    public HttpLoggingFilter httpLoggingFilter() {
        return new HttpLoggingFilter();
    }
}
