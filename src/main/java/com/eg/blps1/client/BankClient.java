package com.eg.blps1.client;

import com.eg.blps1.client.dto.DebitRequest;
import com.eg.blps1.client.dto.DebitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
                        response -> {
                            throw new RuntimeException("Insufficient funds (402)");
                        }
                )
                .onStatus(HttpStatusCode::isError,
                        response -> {
                            throw new RuntimeException("An error occurred: " + response.statusCode());
                        }
                )
                .bodyToMono(DebitResponse.class)
                .block();
    }

    private static final String BANK_OPERATION_DEBIT_LOCAL_PATH = "/operation/debit";
    private static final String BANK_OPERATION_REFUND_LOCAL_PATH = "/operation/refund";

}
