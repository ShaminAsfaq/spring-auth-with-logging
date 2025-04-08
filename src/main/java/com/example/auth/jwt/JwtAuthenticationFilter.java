package com.example.auth.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String INVALID_TOKEN_MESSAGE = "Invalid or expired JWT token";

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = resolveToken(request);

            if (jwt != null && jwtUtil.isTokenValid(jwt)) {
                authenticateUser(jwt, request);
            }

            filterChain.doFilter(request, response);

        } catch (JwtException | AuthenticationException e) {
            handleAuthenticationError(response, e);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private void authenticateUser(String jwt, HttpServletRequest request) {
        String username = jwtUtil.extractUsername(jwt);
        List<String> roles = jwtUtil.extractRoles(jwt);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleAuthenticationError(HttpServletResponse response, Exception e) throws IOException {
        logger.error("Auth error", e);
        response.setStatus(401);
        response.getWriter().write("{\"error\":\"" + INVALID_TOKEN_MESSAGE + "\"}");
        response.getWriter().flush();
    }
}