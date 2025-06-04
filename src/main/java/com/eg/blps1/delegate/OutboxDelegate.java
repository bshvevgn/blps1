package com.eg.blps1.delegate;

import com.eg.blps1.model.Outbox;
import com.eg.blps1.model.enums.OutboxStatus;
import com.eg.blps1.service.KafkaService;
import com.eg.blps1.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class OutboxDelegate implements JavaDelegate {
    private final OutboxService outboxService;
    private final KafkaService kafkaService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("Schedule outbox data..");
        Outbox outbox = outboxService.getScheduleActualProgressOutbox();
        if (outbox == null) { return; }
        kafkaService.sendMessage(outbox.getTopic(), outbox.getPayload());
        outboxService.updateStatus(outbox, OutboxStatus.SUCCESS);
    }
}
