package com.eg.blps1.dto;

import java.time.LocalDate;
import java.util.List;

public record ListingAvailableDatesDto(
        String title,
        List<LocalDate> dates
) {
}
