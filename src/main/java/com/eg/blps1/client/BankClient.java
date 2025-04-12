package com.eg.blps1.client;

import com.eg.blps1.client.dto.DebitRequest;
import com.eg.blps1.client.dto.DebitResponse;
import com.eg.blps1.client.dto.RefundRequest;
import com.eg.blps1.exceptions.PaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class BankClient {
    private final WebClient webClient;

    public DebitResponse debitAmount(DebitRequest request) {
        return webClient
                .post()
                .uri(BANK_OPERATION_DEBIT_LOCAL_PATH)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.PAYMENT_REQUIRED.value(),
                        response -> { throw new PaymentException(); }
                )
                .bodyToMono(DebitResponse.class)
                .block();
    }

    public void refund(RefundRequest request) {
        webClient
                .post()
                .uri(BANK_OPERATION_REFUND_LOCAL_PATH)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.PAYMENT_REQUIRED.value(),
                        response -> { throw new PaymentException(); }
                )
                .toBodilessEntity()
                .block();
    }

    private static final String BANK_OPERATION_DEBIT_LOCAL_PATH = "/operation/debit";
    private static final String BANK_OPERATION_REFUND_LOCAL_PATH = "/operation/refund";

}
