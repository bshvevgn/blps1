package com.eg.blps1.delegate;

import com.eg.blps1.delegate.util.PoolAccessMap;
import com.eg.blps1.model.RoleEnum;
import com.eg.blps1.model.User;
import com.eg.blps1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.camunda.bpm.engine.repository.ProcessDefinition;

import java.util.EnumSet;


@Component
@RequiredArgsConstructor
public class CredentialsChecker implements JavaDelegate {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RepositoryService repositoryService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String processDefinitionId = delegateExecution.getProcessDefinitionId();
        ProcessDefinition definition = repositoryService.getProcessDefinition(processDefinitionId);
        String processName = definition.getName();

        delegateExecution.setVariable("activePool", processName);

        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");

        if (username == null || password == null || processName == null) {
            throw new BpmnError("authError", "Введите имя пользователя и пароль");
        }

        User user = userService.findByUsername(username);
        if (user == null || user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BpmnError("authError", "Неверное имя пользователя или пароль");
        }

        if (user.getRole() == null) {
            throw new BpmnError("authError", "У пользователя не назначена роль");
        }

        EnumSet<RoleEnum> allowedRoles = PoolAccessMap.POOL_ACCESS.get(processName);
        if (allowedRoles == null || !allowedRoles.contains(user.getRole())) {
            throw new BpmnError("authError", "Ваша группа не имеет нужных привилегий");
        }

        delegateExecution.setVariable("username", user.getUsername());
        delegateExecution.setVariable("role", user.getRole().toString());
    }
}