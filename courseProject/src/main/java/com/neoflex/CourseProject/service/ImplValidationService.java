package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.LoanStatementRequestDto;

import com.neoflex.CourseProject.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
@Slf4j
public class ImplValidationService implements Validation {

    private static final String NAMES_REGEX = "^[A-Za-z]{2,30}";
    private static final String PASSPORT_SERIES_REGEX = "^[0-9]{4}";
    private static final String MAIL_REGEX = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$";
    private static final String PASSPORT_NUMBER_REGEX="^[0-9]{6}";

    @Override
    public void resultsOfValidation(LoanStatementRequestDto loanStatementRequestDto){
        log.info(loanStatementRequestDto.toString());
        if(loanStatementRequestDto.getMiddleName() != null) {
            areNamesValid(loanStatementRequestDto.getMiddleName());
        }
        isEmailValid(loanStatementRequestDto.getEmail());
        areNamesValid(loanStatementRequestDto.getFirstName());
        areNamesValid(loanStatementRequestDto.getLastName());
        isPassportSeriesValid(loanStatementRequestDto.getPassportSeries());
        isPassportNumberValid(loanStatementRequestDto.getPassportNumber());
        isAmountValid(loanStatementRequestDto.getAmount());
        isTermValid(loanStatementRequestDto.getTerm());
        isAdult(loanStatementRequestDto.getBirthdate());
        log.info("loanStatementRequestDto is valid");
   }

    private void isEmailValid(String email){
       if(!email.matches(MAIL_REGEX)) {
           log.info("validation exception email is not valid " + email);
           throw new ValidationException("email is not valid " + email);
       }
   }

    private void areNamesValid(String name){
         if(!name.matches(NAMES_REGEX)) {
             log.info("validation exception name is not valid " + name);
             throw new ValidationException("name is not valid " + name);
         }
    }

     private void isPassportSeriesValid(String passportSeries){
        if(!passportSeries.matches(PASSPORT_SERIES_REGEX)) {
            log.info("validation exception passport series is not valid " + passportSeries);
            throw new ValidationException("passport series is not valid " + passportSeries);
        }
    }

     private void isPassportNumberValid(String passportNumber){
         if(!passportNumber.matches(PASSPORT_NUMBER_REGEX)) {
             log.info("validation exception passport number is not valid " + passportNumber);
             throw new ValidationException("passport number is not valid " + passportNumber);
         }
    }

     private void isAmountValid(BigDecimal amount){
       if(amount.compareTo(BigDecimal.valueOf(20000)) < 0) {
           log.info("validation exception amount is too small " + amount);
           throw new ValidationException("amount is too small " + amount);
       }
    }

     private void isTermValid(Integer term){
       if(term<6) {
           log.info("validation exception term is too small " + term);
           throw new ValidationException("term is too small " + term);
       }
    }

     private void isAdult(LocalDate birthday){
       LocalDate currentDate = LocalDate.now();
       if (Period.between(birthday,currentDate).getYears() < 18) {
           log.info("validation exception user is not adult");
           throw new ValidationException("not adult");
       }
    }
}
