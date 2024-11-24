package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.LoanStatementRequestDto;
import com.neoflex.CourseProject.exception.ValidationException;
import com.neoflex.CourseProject.pojo.ValidationProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mock testing the ValidationService")
class ValidationServiceTest {

    @Mock
    private ValidationProperties validationProperties;

    @InjectMocks
    private ValidationServiceImpl implValidationService;

    @Test
    @DisplayName("Mock testing the method resultsOfValidation input data is correct")
    void  resultsOfValidation_shouldNotThrowException(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("email@gmail.com")
                .firstName("firstName")
                .lastName("lastName")
                .middleName("middleName")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        when(validationProperties.getMinimalTerm()).thenReturn(6);
        when(validationProperties.getMinimalAge()).thenReturn(18);
        when(validationProperties.getEmailRegex()).thenReturn("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        when(validationProperties.getPassportNumberRegex()).thenReturn("^[0-9]{6}");
        when(validationProperties.getPassportSeriesRegex()).thenReturn("^[0-9]{4}");
        when(validationProperties.getMinimalAmount()).thenReturn(BigDecimal.valueOf(20000));
        Assertions.assertDoesNotThrow(()-> implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("middle name is null result correct")
    void givenMiddleNameIsNull_whenCallResultsOfValidationMethod_shouldNotThrowValidationException(){
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
        when(validationProperties.getMinimalTerm()).thenReturn(6);
        when(validationProperties.getMinimalAge()).thenReturn(18);
        when(validationProperties.getEmailRegex()).thenReturn("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        when(validationProperties.getPassportNumberRegex()).thenReturn("^[0-9]{6}");
        when(validationProperties.getPassportSeriesRegex()).thenReturn("^[0-9]{4}");
        when(validationProperties.getMinimalAmount()).thenReturn(BigDecimal.valueOf(20000));
        Assertions.assertDoesNotThrow(()-> implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("middle name less then 2 symbols result incorrect")
    void givenMiddleNameContainsLessThenTwoSymbols_whenCallResultsOfValidationMethod_shouldThrowValidationException(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("P")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        Assertions.assertThrows(ValidationException.class, ()->
                implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("middle name more then 30 symbols result incorrect")
    void givenMiddleNameContainsMoreThenThirtySymbols_whenCallResultsOfValidationMethod_shouldThrowValidationException(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("qweqweqweqweqweqweqweqweqweqweqwe")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        Assertions.assertThrows(ValidationException.class, ()->
                implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("amount less then 20000 result incorrect")
    void givenAmountLessTheTwentyThousand_whenCallResultsOfValidationMethod_shouldThrowValidationException(){
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
        when(validationProperties.getEmailRegex()).thenReturn("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        when(validationProperties.getPassportNumberRegex()).thenReturn("^[0-9]{6}");
        when(validationProperties.getPassportSeriesRegex()).thenReturn("^[0-9]{4}");
        when(validationProperties.getMinimalAmount()).thenReturn(BigDecimal.valueOf(20000));
        Assertions.assertThrows(ValidationException.class, ()->
                implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("date does not correspond to 18 years result incorrect")
    void givenDateDoesNotCorrespondToEighteenYears_whenCallResultsOfValidationMethod_shouldThrowValidationException(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.now())
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        when(validationProperties.getMinimalTerm()).thenReturn(6);
        when(validationProperties.getMinimalAge()).thenReturn(18);
        when(validationProperties.getEmailRegex()).thenReturn("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        when(validationProperties.getPassportNumberRegex()).thenReturn("^[0-9]{6}");
        when(validationProperties.getPassportSeriesRegex()).thenReturn("^[0-9]{4}");
        when(validationProperties.getMinimalAmount()).thenReturn(BigDecimal.valueOf(20000));
        Assertions.assertThrows(ValidationException.class, ()->
                implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("email doesn't have the @ symbol result incorrect")
    void givenEmailDoesNotConsistSymbolAt_whenCallResultsOfValidationMethod_shouldThrowValidationException(){
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
        when(validationProperties.getEmailRegex()).thenReturn("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        Assertions.assertThrows(ValidationException.class, ()->
                implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the name is written in Cyrillic result incorrect")
    void givenNameWrittenInCyrillic_whenCallResultsOfValidationMethod_shouldThrowValidationException(){
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
        when(validationProperties.getEmailRegex()).thenReturn("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        Assertions.assertThrows(ValidationException.class, ()->
                implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the passport number contains more then 6 numbers result incorrect")
    void givenPassportNumberContainsMoreThanSixNumbers_whenCallResultsOfValidationMethod_shouldThrowValidationException(){
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
        when(validationProperties.getEmailRegex()).thenReturn("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        when(validationProperties.getPassportNumberRegex()).thenReturn("^[0-9]{6}");
        when(validationProperties.getPassportSeriesRegex()).thenReturn("^[0-9]{4}");
        Assertions.assertThrows(ValidationException.class, ()->
                implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the passport number contains less then 6 numbers result incorrect")
    void givenPassportNumberContainsLessThanSixNumbers_whenCallResultsOfValidationMethod_shouldThrowValidationException(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("12")
                .passportSeries("1234")
                .term(10)
                .build();
        when(validationProperties.getEmailRegex()).thenReturn("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        when(validationProperties.getPassportNumberRegex()).thenReturn("^[0-9]{6}");
        when(validationProperties.getPassportSeriesRegex()).thenReturn("^[0-9]{4}");
        Assertions.assertThrows(ValidationException.class, ()->
                implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the passport series contains more then 4 numbers result incorrect")
    void givenPassportSeriesContainsMoreThanForNumbers_whenCallResultsOfValidationMethod_shouldThrowValidationException(){
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
        when(validationProperties.getEmailRegex()).thenReturn("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        when(validationProperties.getPassportSeriesRegex()).thenReturn("^[0-9]{4}");
        Assertions.assertThrows(ValidationException.class, ()->
                implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the passport series contains more less then 4 numbers result incorrect")
    void givenPassportSeriesContainsLessThanForNumbers_whenCallResultsOfValidationMethod_shouldThrowValidationException(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("12")
                .term(10)
                .build();
        when(validationProperties.getEmailRegex()).thenReturn("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        when(validationProperties.getPassportSeriesRegex()).thenReturn("^[0-9]{4}");
        Assertions.assertThrows(ValidationException.class, ()->
                implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the term less then 6 months result incorrect")
    void givenTermLessThanSix_whenCallResultsOfValidationMethod_shouldThrowValidationException(){
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
        when(validationProperties.getMinimalTerm()).thenReturn(6);
        when(validationProperties.getEmailRegex()).thenReturn("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        when(validationProperties.getPassportNumberRegex()).thenReturn("^[0-9]{6}");
        when(validationProperties.getPassportSeriesRegex()).thenReturn("^[0-9]{4}");
        when(validationProperties.getMinimalAmount()).thenReturn(BigDecimal.valueOf(20000));
        Assertions.assertThrows(ValidationException.class, ()->
                implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the term bigger then 6 months result correct")
    void givenTermBiggerThanSix_whenCallResultsOfValidationMethod_shouldThrowValidationException(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(12)
                .build();
        when(validationProperties.getMinimalTerm()).thenReturn(6);
        when(validationProperties.getEmailRegex()).thenReturn("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        when(validationProperties.getPassportNumberRegex()).thenReturn("^[0-9]{6}");
        when(validationProperties.getPassportSeriesRegex()).thenReturn("^[0-9]{4}");
        when(validationProperties.getMinimalAmount()).thenReturn(BigDecimal.valueOf(20000));
        when(validationProperties.getMinimalTerm()).thenReturn(6);
        Assertions.assertDoesNotThrow(()-> implValidationService.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the term equals 6 months result correct")
    void givenTermEqualsSix_whenCallResultsOfValidationMethod_shouldThrowValidationException(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(12)
                .build();
        when(validationProperties.getMinimalTerm()).thenReturn(6);
        when(validationProperties.getEmailRegex()).thenReturn("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
        when(validationProperties.getNamesRegex()).thenReturn("^[A-Za-z]{2,30}");
        when(validationProperties.getPassportNumberRegex()).thenReturn("^[0-9]{6}");
        when(validationProperties.getPassportSeriesRegex()).thenReturn("^[0-9]{4}");
        when(validationProperties.getMinimalAmount()).thenReturn(BigDecimal.valueOf(20000));
        when(validationProperties.getMinimalTerm()).thenReturn(6);
        Assertions.assertDoesNotThrow(()-> implValidationService.resultsOfValidation(loanStatementRequestDto));
    }
}