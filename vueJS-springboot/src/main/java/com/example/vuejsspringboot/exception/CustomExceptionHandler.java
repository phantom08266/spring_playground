package com.example.vuejsspringboot.exception;

import com.example.vuejsspringboot.exception.dto.MethodValidMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MethodValidMessage methodValidExceptionHandler(MethodArgumentNotValidException e) {
        MethodValidMessage methodValidMessage = new MethodValidMessage(HttpStatus.BAD_REQUEST.value(), "요청값이 올바르지 않습니다");

        e.getFieldErrors().forEach(error -> {
            methodValidMessage.addDetail(error.getField(), error.getDefaultMessage());
        });
        return methodValidMessage;
    }
}
