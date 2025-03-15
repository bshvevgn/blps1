package com.eg.blps1.dto;

import java.time.Instant;

public record ErrorResponse(
        String message,
        Instant time
) {
}
