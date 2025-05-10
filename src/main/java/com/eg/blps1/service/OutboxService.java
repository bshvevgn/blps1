package com.eg.blps1.service;

import com.eg.blps1.mapper.OutboxMapper;
import com.eg.blps1.model.Outbox;
import com.eg.blps1.model.enums.OutboxStatus;
import com.eg.blps1.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxMapper outboxMapper;
    private final OutboxRepository outboxRepository;

    public void create(String topic, String payload) {
        Outbox outbox = outboxMapper.mapToEntity(topic, payload, OutboxStatus.INPROGRESS);
        outboxRepository.save(outbox);
    }

    @Transactional
    public Outbox getScheduleActualProgressOutbox() {
        Outbox outbox = outboxRepository.findByStatusAndRetryTimeBefore(OutboxStatus.INPROGRESS, Instant.now());
        outbox.setRetryTime(Instant.now().plusSeconds(3600));
        return outbox;
    }

    public void updateStatus(Outbox outbox, OutboxStatus outboxStatus) {
        outbox.setStatus(outboxStatus);
        outboxRepository.save(outbox);
    }
}
