package com.eg.blps1.mapper;

import com.eg.blps1.model.Sanction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@AllArgsConstructor
public class SanctionMapper {

    public Sanction mapToEntity(String username, String reason, Instant expiresAt) {
        return new Sanction(username, reason, expiresAt);
    }
}
