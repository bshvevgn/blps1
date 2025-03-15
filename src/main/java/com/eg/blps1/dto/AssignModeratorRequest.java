package com.eg.blps1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AssignModeratorRequest(
        @NotBlank @Min(0)
        Long complaintId
) {
}
