package com.eg.blps1.exceptions;

public class ModeratorNotAssignedComplaintException extends CustomException {
    public ModeratorNotAssignedComplaintException() {
        super("Модератор не назначен на заявку");
    }
}
