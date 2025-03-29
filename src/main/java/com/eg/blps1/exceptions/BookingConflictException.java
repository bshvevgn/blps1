package com.eg.blps1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BookingConflictException extends CustomException {
    public BookingConflictException(String message) {
        super("Невозможно забронировать на выбранные даты: " + message);
    }
}
