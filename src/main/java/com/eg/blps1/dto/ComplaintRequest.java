package com.eg.blps1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ComplaintRequest(
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotBlank @Email
        String defendant
) {
}
