package com.eg.blps1.dto;

import com.eg.blps1.model.ComplaintStatus;
import jakarta.validation.constraints.*;

import java.time.Instant;

public record UpdateStatusRequest(
        @NotBlank @Min(0)
        Long complaintId,
        @NotNull @Pattern(regexp = "APPROVED|REJECTED")
        ComplaintStatus status,
        @Future
        Instant expiresAt
) {
}