package com.eg.blps1.delegate;

import com.eg.blps1.model.User;
import com.eg.blps1.service.SanctionService;
import com.eg.blps1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SanctionCheckerDelegate implements JavaDelegate {

    private final SanctionService sanctionService;
    private final UserService userService;

    @Override
    public void execute(DelegateExecution execution) {
        String username = (String) execution.getVariable("username");
        User user = userService.findByUsername(username);

        boolean hasSanction = sanctionService.hasActiveSanction(user);
        execution.setVariable("hasSanction", hasSanction);
    }
}
