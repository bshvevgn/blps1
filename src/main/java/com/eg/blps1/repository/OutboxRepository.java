package com.eg.blps1.repository;

import com.eg.blps1.model.Outbox;
import com.eg.blps1.model.enums.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {
    Optional<Outbox> findTop1ByStatusAndRetryTimeBefore(OutboxStatus status, Instant retryTime);
}
