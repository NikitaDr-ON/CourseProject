package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.LoanStatementRequestDto;
import com.neoflex.CourseProject.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class ImplValidationServiceTest {

    @InjectMocks
    ImplValidationService implValidationService;

    @Test
    void  resultsOfValidation_shouldNotTrowException(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertDoesNotThrow(()-> implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    void  resultsOfValidation_shouldNotTrowExceptionIfHaveNotMiddleName(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertDoesNotThrow(()-> implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    void  resultsOfValidation_shouldReturnExceptionAtAmount(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(2))
                .birthdate(LocalDate.of(2002,9,23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, ()-> implValidationService.resultsOfValidation(loanStatementRequestDto));
    }
    @Test
    void  resultsOfValidation_shouldReturnExceptionAtBirthday(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2010,9,23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, ()-> implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    void resultsOfValidation_shouldReturnExceptionAtEmail(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("somethingqwe.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, ()-> implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    void resultsOfValidation_shouldReturnExceptionAtFirstName(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("something@qwe.com")
                .firstName("Петр")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, ()-> implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    void resultsOfValidation_shouldReturnExceptionAtMiddleName(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("something@qwe.com")
                .firstName("Petr")
                .lastName("petrov")
                .middleName("Петрович")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, ()-> implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    void  resultsOfValidation_shouldReturnExceptionAtPassportNumber(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("12345346")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, ()-> implValidationService.resultsOfValidation(loanStatementRequestDto) );
    }

    @Test
    void  resultsOfValidation_shouldReturnExceptionAtPassportSeries(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1231231234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, ()-> implValidationService.resultsOfValidation(loanStatementRequestDto) );
    }
    @Test
    void  resultsOfValidation_shouldReturnExceptionAtTerm(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(2)
                .build();
        Assertions.assertThrows(ValidationException.class, ()-> implValidationService.resultsOfValidation(loanStatementRequestDto) );
    }

}