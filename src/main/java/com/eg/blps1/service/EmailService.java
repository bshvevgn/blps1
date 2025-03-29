package com.eg.blps1.service;

import com.eg.blps1.model.Complaint;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;

    @Async
    public void sendStatusChangeMessage(String username, Complaint sanction) {
        sendMessage(
                username,
                "Изменение статуса заявки №" + sanction.getId(),
                "Статус заявки №" + sanction.getId() + " от " + sanction.getCreatedAt() + " изменен.\n\n" +
                        "Новый статус заявки: " + sanction.getStatus()
        );
    }

    private void sendMessage(String mail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}