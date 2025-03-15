package com.eg.blps1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BookingRequest(
        @NotNull @Min(0)
        Long listingId
) {
}
