package com.eg.blps1.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookingRequest(
        @NotNull(message = "ID объявления не может быть пустым") @Min(value = 0, message = "ID объявления не может быть отрицательным")
        Long listingId,
        @NotNull(message = "Дата заезда не может быть пустой") @FutureOrPresent(message = "Дата заезда может быть равна только текущей или большей") LocalDate startDate,
        @NotNull(message = "Дата выезда не может быть пустой") @FutureOrPresent(message = "Дата выезда может быть равна только текущей или большей") LocalDate endDate,
        @NotBlank(message = "Номер карты не может быть пустым")
        String cardNumber,
        @NotBlank(message = "Срок истечения карты не может быть пустым")
        String expirationDate,
        @NotBlank(message = "CVV не может быть пустым")
        String cvv
) {
}
