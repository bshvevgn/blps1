package com.eg.blps1.mapper;

import com.eg.blps1.model.Outbox;
import com.eg.blps1.model.enums.OutboxStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class OutboxMapper {

    public Outbox mapToEntity(String topic, String payload, OutboxStatus status) {
        return new Outbox(
                topic,
                payload,
                status,
                Instant.now()
        );
    }
}
