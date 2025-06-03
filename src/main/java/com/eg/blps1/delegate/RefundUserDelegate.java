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
        String id = (String) execution.getVariable("transactionId");
        if (id != null) {
            try {
                bankService.refund(id);
            } catch (Exception e) {
                throw new BpmnError("paymentError", "Ошибка при возврате средств");
            }
        } else {
            throw new BpmnError("bookingError", "Не удалось списать средства. Повторите попытку позже");
        }
    }
}