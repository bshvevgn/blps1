package com.eg.blps1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void sendMessage(String topic, Object dto) {
        log.info("Sending kafka to topic={{}} and object={}", topic, dto);
        if (dto instanceof String) {
            kafkaTemplate.send(topic, (String) dto);
        } else {
            String message = objectMapper.writeValueAsString(dto);
            kafkaTemplate.send(topic, message);
        }
    }
}
