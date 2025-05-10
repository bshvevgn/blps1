package com.eg.blps1.repository;

import com.eg.blps1.model.Outbox;
import com.eg.blps1.model.enums.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {
    Outbox findByStatusAndRetryTimeBefore(OutboxStatus status, Instant retryTime);
}
