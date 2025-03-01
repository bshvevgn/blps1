package com.eg.blps1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImposeSanctionDto {
    private String username;
    private String reason;
    private String expiresAt;
}
