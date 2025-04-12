package com.example.auth.component;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PrometheusStartupLogger {

    private static final Logger logger = LoggerFactory.getLogger(PrometheusStartupLogger.class);

    private final PrometheusMeterRegistry prometheusMeterRegistry;

    public PrometheusStartupLogger(PrometheusMeterRegistry prometheusMeterRegistry) {
        this.prometheusMeterRegistry = prometheusMeterRegistry;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logPrometheusStatus() {
        if (prometheusMeterRegistry != null) {
            logger.info("PrometheusMeterRegistry initialized successfully.");
        } else {
            logger.warn("PrometheusMeterRegistry not initialized.");
        }
    }
}