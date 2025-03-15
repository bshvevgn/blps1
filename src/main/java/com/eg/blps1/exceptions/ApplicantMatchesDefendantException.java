package com.eg.blps1.exceptions;

public class ApplicantMatchesDefendantException extends CustomException {
    public ApplicantMatchesDefendantException() {
        super("Заявитель и ответчик не могут совпадать");
    }
}
