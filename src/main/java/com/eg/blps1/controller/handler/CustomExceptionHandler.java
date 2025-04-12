package com.eg.blps1.controller.handler;

import com.eg.blps1.dto.ErrorResponse;
import com.eg.blps1.exceptions.CustomException;
import com.eg.blps1.exceptions.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;

@Slf4j
@ControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(HttpMessageNotReadableException ex) {
        loggingException(ex);
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(UsernameNotFoundException ex) {
        loggingException(ex);
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(PaymentException ex) {
        loggingException(ex);
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        loggingException(ex);
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        loggingException(ex);
        return new ResponseEntity<>(new ErrorResponse("Такой страницы не существует", Instant.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
        loggingException(ex);
        return ResponseEntity.badRequest().body(new ErrorResponse("Невалидное тело запроса", Instant.now()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        loggingException(ex);
        return new ResponseEntity<>(new ErrorResponse("Неверный логин или пароль", Instant.now()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleThrowable(Throwable ex) {
        loggingException(ex);
        return ResponseEntity.internalServerError().body(new ErrorResponse("Что-то пошло не так...", Instant.now()));
    }

    private void loggingException(Throwable ex) {
        log.error("Something happens...");
        ex.printStackTrace();
    }
}