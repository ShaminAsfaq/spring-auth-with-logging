package com.example.auth.model.dto;

import com.example.auth.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    @Schema(hidden = true)
    private Long id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(hidden = true)
    private Set<Role> roles;
}
