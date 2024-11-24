package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.LoanStatementRequestDto;
import com.neoflex.CourseProject.exception.ValidationException;
import com.neoflex.CourseProject.pojo.ValidationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
@Slf4j
public class ValidationServiceImpl implements ValidationService {

    private final ValidationProperties validationProperties;

    @Autowired
    public ValidationServiceImpl(ValidationProperties validationProperties){
        this.validationProperties = validationProperties;
    }

    @Override
    public void resultsOfValidation(LoanStatementRequestDto loanStatementRequestDto){
        log.info(loanStatementRequestDto.toString());
        areNamesValid(loanStatementRequestDto.getMiddleName());
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
       if (!email.matches(validationProperties.getEmailRegex())) {
           log.info("validation exception email is not valid " + email);
           throw new ValidationException("mail must be like: example@mail.com");
       }
   }

    private void areNamesValid(String name){
         if (name != null && !name.matches(validationProperties.getNamesRegex())) {
             log.info("validation exception name is not valid " + name);
             throw new ValidationException("The name must be more than one and less than thirty-one" +
                     "characters long and written in Cyrillic" + name);
         }
    }

     private void isPassportSeriesValid(String passportSeries){
        if (!passportSeries.matches(validationProperties.getPassportSeriesRegex())) {
            log.info("validation exception passport series is not valid " + passportSeries);
            throw new ValidationException("passport series must consist of for digits");
        }
    }

     private void isPassportNumberValid(String passportNumber){
         if (!passportNumber.matches(validationProperties.getPassportNumberRegex())) {
             log.info("validation exception passport number is not valid " + passportNumber);
             throw new ValidationException("The passport number must consist of six digits");
         }
    }

     private void isAmountValid(BigDecimal amount){
       if (amount.compareTo(validationProperties.getMinimalAmount()) < 0) {
           log.info("validation exception amount is too small " + amount);
           throw new ValidationException("amount is too small " + amount);
       }
    }

     private void isTermValid(Integer term){
       if (term<validationProperties.getMinimalTerm()) {
           log.info("validation exception term is too small " + term);
           throw new ValidationException("term is too small " + term);
       }
    }

     private void isAdult(LocalDate birthday){
       LocalDate currentDate = LocalDate.now();
       if (Period.between(birthday,currentDate).getYears() < validationProperties.getMinimalAge()) {
           log.info("validation exception user is not adult");
           throw new ValidationException("not adult");
       }
    }
}
