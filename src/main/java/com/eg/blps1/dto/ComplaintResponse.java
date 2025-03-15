package com.eg.blps1.dto;

import com.eg.blps1.model.ComplaintStatus;

import java.time.Instant;

public record ComplaintResponse(
        Long id,
        String title,
        String description,
        ComplaintStatus status,
        String applicant,
        String defendant,
        Instant createdAt
) {
}
