package com.eg.blps1.service;

import com.eg.blps1.mapper.OutboxMapper;
import com.eg.blps1.model.Outbox;
import com.eg.blps1.model.enums.OutboxStatus;
import com.eg.blps1.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

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

    public Outbox getScheduleActualProgressOutbox() {
        Optional<Outbox> outboxOpt = outboxRepository.findTop1ByStatusAndRetryTimeBefore(OutboxStatus.INPROGRESS, Instant.now());
        if (outboxOpt.isEmpty()) {
            return null;
        }
        Outbox outbox = outboxOpt.get();
        outbox.setRetryTime(Instant.now().plusSeconds(3600));
        outboxRepository.save(outbox);
        return outbox;
    }

    public void updateStatus(Outbox outbox, OutboxStatus outboxStatus) {
        outbox.setStatus(outboxStatus);
    }
}
