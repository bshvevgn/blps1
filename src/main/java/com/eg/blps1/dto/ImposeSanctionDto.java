package com.eg.blps1.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record ImposeSanctionDto(
        @NotBlank
        String username,
        @NotBlank
        String reason,
        @Future
        Instant expiresAt
) {
}