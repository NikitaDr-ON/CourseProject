package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {

    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();
    }

    @Test
    void isEmailValid_shouldReturnTrue(){
        boolean email = false;
        try {
            email = validationService.isEmailValid("asd@gmail.com");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertTrue(email);
    }

    @Test
    void isEmailValid_shouldReturnFalse(){
        boolean email = false;
        try {
            email = validationService.isEmailValid("asdgmail.com");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertFalse(email);
    }
    @Test
    void isNamesValid_shouldReturnTrue(){
        boolean name = false;
        try {
            name = validationService.isNamesValid("ivan");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertTrue(name);
    }

    @Test
    void isNamesValid_shouldReturnFalse(){
        boolean name = false;
        try {
            name = validationService.isNamesValid("Иван");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertFalse(name);
    }

    @Test
    void isPassportSeriesValid_shouldReturnTrue(){
        boolean name = false;
        try {
            name = validationService.isPassportSeriesValid("1234");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertTrue(name);
    }

    @Test
    void isPassportSeriesValid_shouldReturnFalse(){
        boolean name = false;
        try {
            name = validationService.isPassportSeriesValid("12345");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertFalse(name);
    }

    @Test
    void isAmountMeetsTheCondition_shouldReturnTrue() {
        boolean amount = false;
        try {
           amount = validationService.isAmountMeetsTheCondition(BigDecimal.valueOf(20000));
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertTrue(amount);
    }

    @Test
    void isAmountMeetsTheCondition_shouldReturnFalse() {
        boolean amount = true;
        try {
            amount = validationService.isAmountMeetsTheCondition(BigDecimal.valueOf(15000));
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertTrue(amount);
    }

    @Test
    void isTermMeetsTheCondition_shouldReturnTrue() {
        boolean term = false;
        try {
            term = validationService.isTermMeetsTheCondition(6);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertTrue(term);
    }

    @Test
    void isTermMeetsTheCondition_shouldReturnFalse() {
        boolean term = true;
        try {
            term = validationService.isTermMeetsTheCondition(5);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertTrue(term);
    }

}