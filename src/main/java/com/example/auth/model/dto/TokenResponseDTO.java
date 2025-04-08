package com.example.auth.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenResponseDTO {
    private final String tokenType;
    private final String accessToken;
    private final Long expiresIn;
    // private final String refreshToken; // If using refresh tokens
}