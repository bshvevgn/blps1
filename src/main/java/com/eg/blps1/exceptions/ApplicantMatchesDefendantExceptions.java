package com.eg.blps1.exceptions;

public class ApplicantMatchesDefendantExceptions extends RuntimeException {
    public ApplicantMatchesDefendantExceptions() {
        super("Заявитель и ответчик не могут совпадать");
    }
}
