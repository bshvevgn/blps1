package com.eg.blps1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ImposeSanctionDto {
    private String username;
    private String reason;
    private Instant expiresAt;
}
