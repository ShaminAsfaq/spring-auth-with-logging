package com.example.auth.config;

import com.example.auth.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .csrf(AbstractHttpConfigurer::disable)  // Using method reference for cleaner code
                .cors(AbstractHttpConfigurer::disable)  // Disabling CORS as well
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/h2-console/**",
                                "/error"  // Allow error page
                        ).permitAll()

                        // All other requests
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers  // 👈 Required for H2 Console frames
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)  // Disable X-Frame-Options
                )
                .securityContext(AbstractHttpConfigurer::disable)
                .requestCache(RequestCacheConfigurer::disable)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
