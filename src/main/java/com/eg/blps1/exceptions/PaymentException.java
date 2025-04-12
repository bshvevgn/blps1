package com.eg.blps1.exceptions;

public class PaymentException extends RuntimeException {
    public PaymentException() {
        super("Банк-партнер не смог списать денежные средства");
    }
}
