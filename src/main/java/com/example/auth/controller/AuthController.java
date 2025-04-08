package com.example.auth.controller;

import com.example.auth.model.dto.LoginDTO;
import com.example.auth.model.dto.UserDTO;
import com.example.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.register(userDTO));
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDTO loginDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.login(loginDTO));
    }
}
