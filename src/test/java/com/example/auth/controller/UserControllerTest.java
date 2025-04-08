package com.example.auth.controller;

import com.example.auth.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    private String validToken;

    @BeforeEach
    public void setUp() {
        // Create mock authorities/roles
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        // Generate token with username and roles
        validToken = jwtUtil.generateToken("testuser", authorities);
    }

    @Test
    @WithMockUser // Simulate an authenticated user
    public void testListAllUsers_Authorized() throws Exception {
        mockMvc.perform(get("/api/user/list")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testListAllUsers_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/user/list")
                .header(HttpHeaders.AUTHORIZATION, "Bearer invalidToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
