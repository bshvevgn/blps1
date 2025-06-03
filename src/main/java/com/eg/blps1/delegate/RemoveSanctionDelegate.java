package com.eg.blps1.delegate;

import com.eg.blps1.model.User;
import com.eg.blps1.repository.SanctionRepository;
import com.eg.blps1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;

@Component
@RequiredArgsConstructor
class RemoveSanctionDelegate implements JavaDelegate {

    private final UserService userService;
    private final SanctionRepository sanctionRepository;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void execute(DelegateExecution execution) {
        String username = (String) execution.getVariable("username");

        try {
            transactionTemplate.execute(status -> {
                User user = userService.findByUsername(username);
                sanctionRepository.deleteAllByUsernameAndExpiresAtAfter(user.getUsername(), Instant.now());
                return null;
            });
        } catch (Exception e) {
            throw new BpmnError("dbError", "Ошибка при удалении санкции: " + e.getMessage());
        }
    }
}