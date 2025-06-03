package com.eg.blps1.delegate;

import com.eg.blps1.config.kafka.KafkaProperty;
import com.eg.blps1.dto.BookingReportDto;
import com.eg.blps1.mapper.KafkaMapper;
import com.eg.blps1.model.Booking;
import com.eg.blps1.service.*;
import com.eg.blps1.utils.SerializationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class SendBookingReportDelegate implements JavaDelegate {
    private final ObjectMapper objectMapper;
    private final KafkaMapper kafkaMapper;
    private final OutboxService outboxService;
    private final KafkaProperty kafkaProperty;

    @Override
    public void execute(DelegateExecution execution) {
        Booking booking = (Booking) execution.getVariable("booking");

        try {
            BookingReportDto dto = kafkaMapper.mapToBookingReport(booking);
            String payload = SerializationUtils.getString(objectMapper, dto);
            outboxService.create(kafkaProperty.getTopics().getBookingReport().getName(), payload);
        } catch (Exception e) {
            throw new BpmnError("kafkaError", "Ошибка при отправке отчета о бронировании");
        }
    }
}