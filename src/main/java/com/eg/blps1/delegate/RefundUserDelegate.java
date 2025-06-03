package com.eg.blps1.delegate;

import com.eg.blps1.client.dto.DebitResponse;
import com.eg.blps1.service.*;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RefundUserDelegate implements JavaDelegate {
    private final BankService bankService;

    @Override
    public void execute(DelegateExecution execution) {
        DebitResponse response = (DebitResponse) execution.getVariable("debitResponse");
        if (response != null) {
            try {
                bankService.refund(response.transactionId());
            } catch (Exception e) {
                throw new BpmnError("paymentError", "Ошибка при возврате средств");
            }
        }
    }
}