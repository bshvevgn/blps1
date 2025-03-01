package com.eg.blps1.dto;

import com.eg.blps1.model.RequestStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusDto {
    private Long requestId;
    private RequestStatus status;
    private Long userId;
    private String expiresAt;
}
