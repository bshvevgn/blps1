package com.eg.blps1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ListingRequest(
        @NotBlank
        String address,
        @Min(0)
        double price,
        @NotBlank
        String note
) {
}