package com.eg.blps1.dto;

public record ListingResponse(
        Long id,
        String address,
        double price,
        String note
) {
}
