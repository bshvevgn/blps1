package com.eg.blps1.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookingRequest(
        @NotNull @Min(0)
        Long listingId,
        @NotNull @Future LocalDate startDate,
        @NotNull @Future LocalDate endDate
) {
}
