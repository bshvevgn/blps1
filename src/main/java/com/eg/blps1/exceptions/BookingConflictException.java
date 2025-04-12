package com.eg.blps1.exceptions;

public class BookingConflictException extends CustomException {
    public BookingConflictException() {
        super("Объявление уже забронировано в указанный период.");
    }
}
