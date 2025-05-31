package com.eg.blps1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ListingRequest(
        @NotBlank(message = "Адрес не может быть пустым")
        String address,
        @Min(value = 0, message = "Цена должна быть положительным числом")
        double price,
        @NotBlank(message = "Описание объявления не может быть пустым")
        String note
) {
}