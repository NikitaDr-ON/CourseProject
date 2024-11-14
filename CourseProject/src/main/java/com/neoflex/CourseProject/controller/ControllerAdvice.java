package com.neoflex.CourseProject.controller;


import com.neoflex.CourseProject.dto.Response;
import com.neoflex.CourseProject.exception.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;


@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Response> handleValidationException(ValidationException e){
        Response response = new Response(e.getMessage());
        return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);

    }
}
