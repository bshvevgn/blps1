package com.eg.blps1.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingReportDto(
        String address,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalPrice,
        String contactEmail,
        String email
) {
}