package com.eg.blps1.client.dto;

import java.math.BigDecimal;

public record DebitRequest(
        BigDecimal amount,
        String cardNumber,
        String expirationDate,
        String cvv
) {
}
