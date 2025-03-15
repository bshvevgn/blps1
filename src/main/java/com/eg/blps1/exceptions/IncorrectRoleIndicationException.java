package com.eg.blps1.exceptions;

public class IncorrectRoleIndicationException extends CustomException {
    public IncorrectRoleIndicationException() {
        super("Вы не можете завести жалобу на пользователя с такой ролью");
    }
}
