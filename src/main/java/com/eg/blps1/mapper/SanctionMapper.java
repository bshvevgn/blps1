package com.eg.blps1.mapper;

import com.eg.blps1.model.Sanction;
import com.eg.blps1.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@AllArgsConstructor
public class SanctionMapper {

    public Sanction mapToEntity(User user, String reason, Instant expiresAt) {
        return new Sanction(user, reason, expiresAt);
    }
}
