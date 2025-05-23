package com.eg.blps1.schedule;

import com.eg.blps1.model.Outbox;
import com.eg.blps1.model.enums.OutboxStatus;
import com.eg.blps1.service.KafkaService;
import com.eg.blps1.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class OutboxSchedule {
    private final OutboxService outboxService;
    private final KafkaService kafkaService;

    @Scheduled(fixedRate = 10000)
    public void outboxSchedule() {
        log.info("Schedule outbox data..");
        Outbox outbox = outboxService.getScheduleActualProgressOutbox();
        kafkaService.sendMessage(outbox.getTopic(), outbox.getPayload());
        outboxService.updateStatus(outbox, OutboxStatus.SUCCESS);
    }
}
