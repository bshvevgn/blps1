package com.eg.blps1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AssignModeratorRequest(
        @NotNull @Min(0)
        Long complaintId
) {
}
