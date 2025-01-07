package com.neoflex.CourseProject.validation;

import com.neoflex.CourseProject.dto.LoanStatementRequestDto;
import com.neoflex.CourseProject.exception.ValidationException;
import com.neoflex.CourseProject.model.ValidationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
@Slf4j
public class PreScoringValidationServiceImpl implements PreScoringValidationService {

    private final ValidationProperties validationProperties;

    @Autowired
    public PreScoringValidationServiceImpl(ValidationProperties validationProperties) {
        this.validationProperties = validationProperties;
    }

    @Override
    public void resultsOfValidation(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("{}", loanStatementRequestDto);
        isFirstNameValid(loanStatementRequestDto.getFirstName());
        isLastNameValid(loanStatementRequestDto.getLastName());
        isMiddleNameValid(loanStatementRequestDto.getMiddleName());
        isEmailValid(loanStatementRequestDto.getEmail());
        isPassportSeriesValid(loanStatementRequestDto.getPassportSeries());
        isPassportNumberValid(loanStatementRequestDto.getPassportNumber());
        isAmountValid(loanStatementRequestDto.getAmount());
        isTermValid(loanStatementRequestDto.getTerm());
        isAdult(loanStatementRequestDto.getBirthdate());
        log.info("LoanStatementRequestDto валидный");
    }

    private void isEmailValid(String email) {
        if (!email.contains("@")) {
            log.info("Почта {} не прошла валидацию на символ @", email);
            throw new ValidationException("Почта должна содержать знак '@'.");
        }
        if (email.indexOf('@') > email.lastIndexOf('.')) {
            log.info("Почта {} не прошла валидацию на доменное имя", email);
            throw new ValidationException("Почта должна содержать знак '.' после знака '@'.");
        }
        if (!email.matches(validationProperties.getEmailRegex())) {
            log.info("Почта {} не прошла валидацию на латинские буквы.", email);
            throw new ValidationException("При заполнении почты следует использовать латинские буквы.");
        }
    }

    private void isMiddleNameValid(String middleName) {
        if (middleName != null) {
            if (!middleName.matches(validationProperties.getNamesRegex())) {
                log.info("отчество {} не прошло валидацию на латинские буквы", middleName);
                throw new ValidationException("При заполнении отчества следует использовать латинские буквы.");
            } else if (middleName.length() < validationProperties.getNameMinSymbolsRestriction()) {
                log.info("отчество {} не прошло валидацию на минимальное количество букв", middleName);
                throw new ValidationException("Минимальная длина отчества составляет "
                        + validationProperties.getNameMinSymbolsRestriction() + " символа.");
            } else if (middleName.length() > validationProperties.getNameMaxSymbolsRestriction()) {
                log.info("отчество {} не прошло валидацию на максимальное количество букв", middleName);
                throw new ValidationException("Максимальная длина отчества составляет "
                        + validationProperties.getNameMaxSymbolsRestriction() + " символов.");
            }
        }
    }

    private void isFirstNameValid(String firstName) {
        if (!firstName.matches(validationProperties.getNamesRegex())) {
            log.info("имя {} не прошло валидацию на латинские буквы", firstName);
            throw new ValidationException("При заполнении отчества следует использовать латинские буквы.");
        } else if (firstName.length() < validationProperties.getNameMinSymbolsRestriction()) {
            log.info("имя {} не прошло валидацию на минимальное количество букв", firstName);
            throw new ValidationException("Минимальная длина отчества составляет "
                    + validationProperties.getNameMinSymbolsRestriction() + " символа.");
        } else if (firstName.length() > validationProperties.getNameMaxSymbolsRestriction()) {
            log.info("имя {} не прошло валидацию на максимальное количество букв", firstName);
            throw new ValidationException("Максимальная длина имени составляет "
                    + validationProperties.getNameMaxSymbolsRestriction() + " символов.");
        }
    }

    private void isLastNameValid(String lastName) {
        if (!lastName.matches(validationProperties.getNamesRegex())) {
            log.info("фамилия {} не прошла валидацию на латинские буквы", lastName);
            throw new ValidationException("При заполнении отчества следует использовать латинские буквы.");
        } else if (lastName.length() < validationProperties.getNameMinSymbolsRestriction()) {
            log.info("фамилия {} не прошла валидацию на минимальное количество букв", lastName);
            throw new ValidationException("Минимальная длина отчества составляет "
                    + validationProperties.getNameMinSymbolsRestriction() + " символа.");
        } else if (lastName.length() > validationProperties.getNameMaxSymbolsRestriction()) {
            log.info("фамилия {} не прошла валидацию на максимальное количество букв", lastName);
            throw new ValidationException("Максимальная длина фамилии составляет "
                    + validationProperties.getNameMaxSymbolsRestriction() + " символов.");
        }
    }

    private void isPassportSeriesValid(String passportSeries) {
        if (!passportSeries.matches(validationProperties.getPassportSeriesRegex())) {
            log.info("серия паспорта должна содержать только цифры {}", passportSeries);
            throw new ValidationException("При заполнении серии паспорта следует использовать только цифры");
        }
        if (passportSeries.length() != 4) {
            log.info("серия паспорта {} не прошла валидацию на количество цифр", passportSeries);
            throw new ValidationException("Максимальная длина серии паспорта составляет "
                    + validationProperties.getCountOfPassportSeriesDigits() + " цифр.");
        }
    }

    private void isPassportNumberValid(String passportNumber) {
        if (!passportNumber.matches(validationProperties.getPassportNumberRegex())) {
            log.info("номер паспорта должен содержать только цифры {}", passportNumber);
            throw new ValidationException("При заполнении номера паспорта следует использовать только цифры.");
        }
        if (passportNumber.length() != 6) {
            log.info("номер паспорта {} не прошел валидацию на количество цифр", passportNumber);
            throw new ValidationException("Максимальная длина номера паспорта составляет "
                    + validationProperties.getCountOfPassportNumberDigits() + " цифр.");
        }
    }

    private void isAmountValid(BigDecimal amount) {
        if (amount.compareTo(validationProperties.getMinimalAmount()) < 0) {
            log.info("сумма займа {} слишком мала", amount);
            throw new ValidationException("Сумма займа должна быть больше или равна"
                    + validationProperties.getMinimalAmount());
        }
    }

    private void isTermValid(Integer term) {
        if (term < validationProperties.getMinimalTerm()) {
            log.info("срок займа {} слишком мал", term);
            throw new ValidationException("Срок займа должен быть больше или равен "
                    + validationProperties.getMinimalTerm() + " месяцам.");
        }
    }

    private void isAdult(LocalDate birthday) {
        LocalDate currentDate = LocalDate.now();
        if (Period.between(birthday, currentDate).getYears() < validationProperties.getMinimalAge()) {
            log.info("клиент не прошел валидацию на возраст {}", Period.between(birthday, currentDate).getYears());
            throw new ValidationException("клиент должен быть совершеннолетним.");
        }
    }
}
