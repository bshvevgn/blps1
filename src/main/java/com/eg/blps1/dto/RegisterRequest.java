package com.eg.blps1.dto;

import com.eg.blps1.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank @Email
        String username,
        @NotBlank @Min(5)
        String password,
        Role role
) {}
