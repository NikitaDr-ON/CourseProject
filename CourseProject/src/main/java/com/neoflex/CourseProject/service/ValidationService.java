package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.LoanStatementRequestDto;

import com.neoflex.CourseProject.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidationService {

    public Boolean resultsOfValidation(LoanStatementRequestDto loanStatementRequestDto) throws ValidationException {
        boolean resultsOfValidations=true;
       Boolean[] testResults;
       if(loanStatementRequestDto.getMiddleName() != null){
           int maxCountConditions = 9;
           testResults = new Boolean[maxCountConditions];
           testResults[8] = isNamesValid(loanStatementRequestDto.getMiddleName());
       }else{
           int minCountConditions = 8;
           testResults = new Boolean[minCountConditions];
       }
       testResults[0] = isEmailValid(loanStatementRequestDto.getEmail());
       testResults[1] = isNamesValid(loanStatementRequestDto.getFirstName());
       testResults[2] = isNamesValid(loanStatementRequestDto.getLastName());
       testResults[3] = isPassportSeriesValid(loanStatementRequestDto.getPassportSeries());
       testResults[4] = isPassportNumberValid(loanStatementRequestDto.getPassportNumber());
       testResults[5] = isAmountMeetsTheCondition(loanStatementRequestDto.getAmount());
       testResults[6] = isTermMeetsTheCondition(loanStatementRequestDto.getTerm());
       testResults[7] = isAdult(loanStatementRequestDto.getBirthdate());
       return resultsOfValidations;
   }

    Boolean isEmailValid(String email) throws ValidationException {
       String mailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
       Pattern pattern = Pattern.compile(mailRegex);
       Matcher matcher = pattern.matcher(email);
       if(matcher.matches())
       return true;
       else
           throw new ValidationException("email is not valid " + email);
   }

     Boolean isNamesValid(String name) throws ValidationException {
        String nameRegex = "^[A-Za-z]{2,30}";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);
         if(matcher.matches())
             return true;
         else
             throw new ValidationException("name is not valid " + name);
    }

     Boolean isPassportSeriesValid(String passportSeries) throws ValidationException {
        String passportSeriesRegex="^[0-9]{4}";
        Pattern pattern = Pattern.compile(passportSeriesRegex);
        Matcher matcher = pattern.matcher(passportSeries);
         if(matcher.matches())
             return true;
         else
             throw new ValidationException("passport series is not valid " + passportSeries);
    }

     Boolean isPassportNumberValid(String passportNumber) throws ValidationException {
        String passportSeriesRegex="^[0-9]{4}";
        Pattern pattern = Pattern.compile(passportSeriesRegex);
        Matcher matcher = pattern.matcher(passportNumber);
         if(matcher.matches())
             return true;
         else
             throw new ValidationException("passport number is not valid " + passportNumber);
    }

     Boolean isAmountMeetsTheCondition(BigDecimal amount) throws ValidationException {
       if(amount.compareTo(BigDecimal.valueOf(20000)) >= 0)
           return true;
       else
           throw new ValidationException("amount is too small " + amount);
    }

     Boolean isTermMeetsTheCondition(Integer term) throws ValidationException {
       if(term>=6)
           return true;
       else
           throw new ValidationException("term is too small " + term);
    }

     Boolean isAdult(LocalDate birthday) throws ValidationException {
       LocalDate currentDate = LocalDate.now();
       int year = currentDate.minusYears(birthday.getYear()).getYear();
       int month = currentDate.minusMonths(birthday.getMonthValue()).getMonthValue();
       int day = currentDate.minusDays(birthday.getDayOfYear()).getDayOfYear();
       if ((LocalDate.of(year,month,day).getYear() >=18))
       return true;
       else
           throw new ValidationException("not adult");
    }

}
