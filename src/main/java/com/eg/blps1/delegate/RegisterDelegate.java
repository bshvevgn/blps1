package com.eg.blps1.delegate;

import com.eg.blps1.dto.RegisterRequest;
import com.eg.blps1.model.RoleEnum;
import com.eg.blps1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RegisterDelegate implements JavaDelegate {
    private final UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");
        RoleEnum role = RoleEnum.valueOf((String) delegateExecution.getVariable("role"));

        userService.registerUser(new RegisterRequest(username, password, role));
        delegateExecution.setVariable("statusMessage", "Добро пожаловать, " + username);
    }
}
