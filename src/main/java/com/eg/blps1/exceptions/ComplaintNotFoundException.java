package com.eg.blps1.exceptions;

public class ComplaintNotFoundException extends CustomException {
    public ComplaintNotFoundException(Long complaintId) {
        super("Заявки с таким id={" + complaintId + "} не существует");
    }
}
