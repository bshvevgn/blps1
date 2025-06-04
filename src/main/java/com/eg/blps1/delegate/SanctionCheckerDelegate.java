package com.eg.blps1.delegate;

import com.eg.blps1.model.User;
import com.eg.blps1.repository.SanctionRepository;
import com.eg.blps1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
class SanctionCheckerDelegate implements JavaDelegate {


    private final UserService userService;
    private final SanctionRepository sanctionRepository;

    @Override
    public void execute(DelegateExecution execution) {
        String username = (String) execution.getVariable("username");

        try {
            User user = userService.findByUsername(username);
            boolean hasSanction = !sanctionRepository.findByUsernameAndExpiresAtAfter(user.getUsername(), Instant.now()).isEmpty();
            execution.setVariable("hasSanctions", hasSanction);
            execution.setVariable("statusMessage", "Вы не можете выполнить действие из-за наличия ограничений");
        } catch (Exception e) {
            throw new BpmnError("dbError", "Ошибка при проверке санкции: " + e.getMessage());
        }
    }
}
