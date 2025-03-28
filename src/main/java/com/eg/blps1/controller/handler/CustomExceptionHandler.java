package com.eg.blps1.controller.handler;

import com.eg.blps1.dto.ErrorResponse;
import com.eg.blps1.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;

@Slf4j
@ControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleException(Throwable ex) {
        log.error("Something happens...");
        log.error(ex.getMessage());
        if (ex instanceof CustomException) {
            return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage(), Instant.now()));
        }
        else if (ex instanceof NoResourceFoundException) {
            return new ResponseEntity<>(new ErrorResponse("Такой страницы не существует", Instant.now()), HttpStatus.NOT_FOUND);
        }
        else if (ex instanceof BindException) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Невалидное тело запроса", Instant.now()));
        }
        else if (ex instanceof BadCredentialsException) {
            return new ResponseEntity<>(new ErrorResponse("Неверный логин или пароль", Instant.now()), HttpStatus.UNAUTHORIZED);

        }
        else {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Что-то пошло не так...", Instant.now()));
        }
    }

}