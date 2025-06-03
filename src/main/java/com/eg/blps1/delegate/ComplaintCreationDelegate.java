package com.eg.blps1.delegate;

import com.eg.blps1.dto.ComplaintRequest;
import com.eg.blps1.mapper.ComplaintMapper;
import com.eg.blps1.model.Complaint;
import com.eg.blps1.model.RoleEnum;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.ComplaintRepository;
import com.eg.blps1.service.SpamService;
import com.eg.blps1.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ComplaintCreationDelegate implements JavaDelegate {

    private final ComplaintMapper complaintMapper;
    private final UserService userService;
    private final SpamService spamService;
    private final ComplaintRepository complaintRepository;
    private final Validator validator;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            String applicantUsername = (String) execution.getVariable("username");
            String defendantUsername = (String) execution.getVariable("defendant");
            String title = (String) execution.getVariable("title");
            String description = (String) execution.getVariable("description");

            ComplaintRequest complaintRequest = new ComplaintRequest(title, description, defendantUsername);

            Set<ConstraintViolation<ComplaintRequest>> violations = validator.validate(complaintRequest);
            if (!violations.isEmpty()) {
                String errorMessage = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining("; "));
                throw new BpmnError("validationError", errorMessage);
            }

            User applicant = userService.findByUsername(applicantUsername);
            User defendant = userService.findByUsername(defendantUsername);

            if (Objects.equals(applicant.getUsername(), defendant.getUsername())) {
                throw new BpmnError("validationError", "Жалоба на самого себя невозможна");
            }

            if (!((applicant.getRole() == RoleEnum.ROLE_USER && defendant.getRole() == RoleEnum.ROLE_LANDLORD) ||
                    (applicant.getRole() == RoleEnum.ROLE_LANDLORD && defendant.getRole() == RoleEnum.ROLE_USER))) {
                throw new BpmnError("validationError", "Вы не можете подать жалобу на пользователя из этой группы");
            }

            Complaint complaint = complaintMapper.mapToEntity(complaintRequest, applicant, defendant);
            complaintRepository.save(complaint);

            execution.setVariable("complaintId", complaint.getId());
            execution.setVariable("statusMessage", "Жалоба успешно подана");

        } catch (BpmnError e) {
            throw e;
        } catch (Exception e) {
            throw new BpmnError("complaintCreationError", "Ошибка при создании жалобы: " + e.getMessage());
        }
    }
}
