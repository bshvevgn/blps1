package com.eg.blps1.service;

import com.eg.blps1.client.BankClient;
import com.eg.blps1.client.dto.DebitRequest;
import com.eg.blps1.client.dto.DebitResponse;
import com.eg.blps1.client.dto.RefundRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankService {
    private final BankClient bankClient;

    public DebitResponse debit(DebitRequest request) {
        log.info("Sending request to bank to debit money..");
        return bankClient.debitAmount(request);
    }

    public void refund(String transactionId) {
        log.info("Sending request to bank to refund money..");
        RefundRequest request = new RefundRequest(transactionId);
        bankClient.refund(request);
    }
}
