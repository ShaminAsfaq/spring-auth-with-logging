package com.example.auth.config;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component // Intercepts all requests
public class HttpLoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(HttpLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();

        // Skip logging for /actuator/prometheus
        if (path.contains("/actuator/prometheus") || path.contains("/actuator/health")) {
            //  Skipping LOG data for this URL
            return;
        }

        // Get the HTTP method (e.g., GET, POST)
        String method = httpRequest.getMethod();

        // Optionally get the requestId from the header or generate one
        String requestId = httpRequest.getHeader("X-Request-ID");
        if (requestId == null) {
            requestId = UUID.randomUUID().toString(); // Generate if not present
        }

        String API = httpRequest.getRequestURI();
        String ipAddress = httpRequest.getRemoteAddr();
        // Get IP from headers (if behind proxy/LB)
        if (httpRequest.getHeader("X-Forwarded-For") != null) {
            ipAddress = httpRequest.getHeader("X-Forwarded-For").split(",")[0]; // First IP in chain
        } else if (httpRequest.getHeader("X-Real-IP") != null) {
            ipAddress = httpRequest.getHeader("X-Real-IP");
        }

        // Add the method and requestId to MDC for structured logging
        MDC.put("method", method);
        MDC.put("requestId", requestId);
        MDC.put("api", API);
        MDC.put("ipAddress", ipAddress);

        // Proceed with the chain
        try {
            chain.doFilter(request, response); // Continue filter chain

        } finally {
            // Clean up MDC
            MDC.clear();
        }
    }

    @Override
    public void destroy() {
    }
}
