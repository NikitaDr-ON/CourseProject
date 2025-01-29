package com.neoflex.statement.controller;

import com.neoflex.statement.exception.Response;
import com.neoflex.statement.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Response> handleValidationException(ValidationException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleConflictException(Exception e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}