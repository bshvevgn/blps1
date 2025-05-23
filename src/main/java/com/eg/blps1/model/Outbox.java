package com.eg.blps1.model;

import com.eg.blps1.model.enums.OutboxStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Table(name = "outbox")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Outbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String topic;
    private String payload;
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;
    private Instant retryTime;

    public Outbox(String topic, String payload, OutboxStatus status, Instant retryTime) {
        this.topic = topic;
        this.payload = payload;
        this.status = status;
        this.retryTime = retryTime;
    }
}
