package com.eg.blps1.exceptions;

public class ModeratorNotAssignedComplaintException extends RuntimeException {
    public ModeratorNotAssignedComplaintException() {
        super("Модератор не назначен на заявку");
    }
}
