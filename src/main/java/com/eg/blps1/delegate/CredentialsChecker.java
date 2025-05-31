package com.eg.blps1.delegate;

import com.eg.blps1.model.User;
import com.eg.blps1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CredentialsChecker implements JavaDelegate {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");
        User user = userService.findByUsername(username);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BpmnError("userNotFound", "Неверный логин или пароль");
        }
        delegateExecution.setVariable("username", user.getUsername());
        delegateExecution.setVariable("role", user.getRole().toString());
    }
}