package com.eg.blps1.delegate;

import com.eg.blps1.dto.ImposeSanctionDto;
import com.eg.blps1.mapper.SanctionMapper;
import com.eg.blps1.model.Sanction;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.SanctionRepository;
import com.eg.blps1.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ImposeSanctionDelegate implements JavaDelegate {

    private final SanctionMapper sanctionMapper;
    private final UserService userService;
    private final SanctionRepository sanctionRepository;
    private final Validator validator;

    @Override
    public void execute(DelegateExecution execution) {
        String username = (String) execution.getVariable("username");
        String reason = (String) execution.getVariable("reason");
        String expiresAtStr = (String) execution.getVariable("expiresAt");

        Instant expiresAt;
        try {
            expiresAt = Instant.parse(expiresAtStr);
        } catch (Exception e) {
            throw new BpmnError("validationError", "Некорректная дата истечения санкции");
        }

        ImposeSanctionDto dto = new ImposeSanctionDto(username, reason, expiresAt);

        Set<ConstraintViolation<ImposeSanctionDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            throw new BpmnError("validationError", errorMessage);
        }

        try {
            User user = userService.findByUsername(username);
            Sanction sanction = sanctionMapper.mapToEntity(user.getUsername(), reason, expiresAt);
            sanctionRepository.save(sanction);
            execution.setVariable("statusMessage", "Ограничение успешно наложено");
        } catch (Exception e) {
            throw new BpmnError("dbError", "Ошибка при наложении санкции: " + e.getMessage());
        }
    }
}