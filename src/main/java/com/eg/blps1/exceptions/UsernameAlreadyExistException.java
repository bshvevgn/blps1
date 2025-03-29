package com.eg.blps1.exceptions;

public class UsernameAlreadyExistException extends CustomException {
    public UsernameAlreadyExistException() {
        super("Пользователь с таким именем уже существует");
    }
}