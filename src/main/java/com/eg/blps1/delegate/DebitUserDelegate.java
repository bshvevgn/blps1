package com.eg.blps1.delegate;

import com.eg.blps1.client.dto.DebitRequest;
import com.eg.blps1.client.dto.DebitResponse;
import com.eg.blps1.dto.BookingRequest;
import com.eg.blps1.mapper.BankMapper;
import com.eg.blps1.model.Listing;
import com.eg.blps1.service.BankService;
import com.eg.blps1.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
class DebitUserDelegate implements JavaDelegate {

    private final BankService bankService;
    private final BankMapper bankMapper;
    private final ListingService listingService;
    private final RefundUserDelegate refundUserDelegate;

    @Override
    public void execute(DelegateExecution execution) {
        Long listingId = Long.parseLong((String) execution.getVariable("listingId"));
        LocalDate startDate = LocalDate.parse((String) execution.getVariable("startDate"));
        LocalDate endDate = LocalDate.parse((String) execution.getVariable("endDate"));
        String cardNumber = (String) execution.getVariable("cardNumber");
        String expirationDate = (String) execution.getVariable("expirationDate");
        String cvv = (String) execution.getVariable("cvv");

        BookingRequest request = new BookingRequest(listingId, startDate, endDate, cardNumber, expirationDate, cvv);
        Listing listing = listingService.findById(request.listingId());
        DebitRequest debitRequest = bankMapper.mapToDebitRequest(request, listing);

        try {
            DebitResponse response = bankService.debit(debitRequest);
            execution.setVariable("debitResponse", response);
        } catch (Exception e) {
            try {
                refundUserDelegate.execute(execution);
            } catch (Exception refundError) {
                System.err.println("Ошибка при возврате средств: " + refundError.getMessage());
            }

            throw new BpmnError("paymentError", "Ошибка при списании средств: " + e.getMessage());
        }
    }
}
