package com.eg.blps1.listener;

import com.eg.blps1.dto.EmailMessageDto;
import com.eg.blps1.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSenderListener {
    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @SneakyThrows
    @KafkaListener(topics = "${spring.kafka.topics.email-sender.name}", containerFactory = "kafkaListenerManualCommitContainerFactory")
    public void readMessages(@Payload String data, Acknowledgment acknowledgment) {
        log.info("Consumed email message: {}", data);
        EmailMessageDto emailMessageDto = objectMapper.readValue(data, EmailMessageDto.class);
        emailService.sendHtmlEmail(emailMessageDto.email(), emailMessageDto.title(), emailMessageDto.message());
        acknowledgment.acknowledge();
    }
}
