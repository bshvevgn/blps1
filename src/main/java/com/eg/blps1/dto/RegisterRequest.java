package com.eg.blps1.dto;

import com.eg.blps1.model.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest (
    @NotBlank @Email
    String username,
    @Size(min = 5)
    String password,
    RoleEnum role
){}
