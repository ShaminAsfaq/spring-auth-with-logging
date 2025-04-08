package com.example.auth.controller;

import com.example.auth.jwt.JwtUtil;
import com.example.auth.model.dto.UserDTO;
import com.example.auth.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "bearer-jwt")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<List<UserDTO>> updatePassword() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserList());
    }
}
