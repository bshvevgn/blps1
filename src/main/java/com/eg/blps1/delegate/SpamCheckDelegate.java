package com.eg.blps1.delegate;

import com.eg.blps1.exceptions.SpamComplaintsException;
import com.eg.blps1.model.ComplaintStatus;
import com.eg.blps1.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SpamCheckDelegate implements JavaDelegate {

    private final ComplaintRepository complaintRepository;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            String username = (String) execution.getVariable("username");

            long activeRequestsCount = complaintRepository.countByApplicantAndStatusIn(
                    username,
                    List.of(ComplaintStatus.CREATED, ComplaintStatus.ASSIGNED)
            );

            if (activeRequestsCount >= 5) {
                throw new BpmnError("spamDetectedError", "У вас слишком много активных жалоб");
            }

        } catch (Exception e) {
            throw new BpmnError("complaintCheckError", "Ошибка при проверке жалоб: " + e.getMessage());
        }
    }
}
