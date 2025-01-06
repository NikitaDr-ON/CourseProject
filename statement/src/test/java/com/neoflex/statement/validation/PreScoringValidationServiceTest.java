package com.neoflex.statement.validation;

import com.neoflex.statement.dto.LoanStatementRequestDto;
import com.neoflex.statement.exception.ValidationException;
import com.neoflex.statement.model.ValidationProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mock testing the ValidationService")
class PreScoringValidationServiceTest {

    @Mock
    private ValidationProperties validationProperties;

    @InjectMocks
    private PreScoringValidationService preScoringValidationServiceIml = new PreScoringValidationServiceImpl(validationProperties);

    @BeforeEach
    void beforeEach() {
        String namesRegex = "^[A-Za-z]{2,30}";
        String passportSeriesRegex = "^[0-9]{4}";
        String emailRegex = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$";
        String passportNumberRegex = "^[0-9]{6}";
        BigDecimal minimalAmount = BigDecimal.valueOf(20000);
        int minimalTerm = 6;
        int minimalAge = 18;
        int nameMinSymbolsRestriction = 2;
        int nameMaxSymbolsRestriction = 30;
        int countOfPassportSeriesDigits = 4;
        int countOfPassportNumberDigits = 6;
        validationProperties = new ValidationProperties(namesRegex, passportSeriesRegex, emailRegex,
                passportNumberRegex, minimalAmount, minimalTerm, minimalAge, nameMinSymbolsRestriction,
                nameMaxSymbolsRestriction, countOfPassportSeriesDigits, countOfPassportNumberDigits);
        ReflectionTestUtils.setField(preScoringValidationServiceIml, "validationProperties", validationProperties);
    }

    @Test
    @DisplayName("Mock testing the method resultsOfValidation input data is correct")
    void resultsOfValidation_shouldNotThrowException() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .email("email@gmail.com")
                .firstName("firstName")
                .lastName("lastName")
                .middleName("middleName")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertDoesNotThrow(() -> preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("middle name is null result correct")
    void givenMiddleNameIsNull_whenCallResultsOfValidationMethod_shouldNotThrowValidationException() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertDoesNotThrow(() -> preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("middle name less than 2 symbols result incorrect")
    void givenMiddleNameContainsLessThanTwoSymbols_whenCallResultsOfValidationMethod_shouldThrowValidationException() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("P")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, () ->
                preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("middle name more than 30 symbols result incorrect")
    void givenMiddleNameContainsMoreThanThirtySymbols_whenCallResultsOfValidationMethod_shouldThrowValidationException() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("qweqweqweqweqweqweqweqweqweqweqwe")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, () ->
                preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("amount less than 20000 result incorrect")
    void givenAmountLessThanTwentyThousand_whenCallResultsOfValidationMethod_shouldThrowValidationException() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(2))
                .birthdate(LocalDate.of(2002, 9, 23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, () ->
                preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("date does not correspond to 18 years result incorrect")
    void givenDateDoesNotCorrespondToEighteenYears_whenCallResultsOfValidationMethod_shouldThrowValidationException() {
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
        Assertions.assertThrows(ValidationException.class, () ->
                preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("email doesn't have the @ symbol result incorrect")
    void givenEmailDoesNotConsistSymbolAt_whenCallResultsOfValidationMethod_shouldThrowValidationException() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .email("somethingqwe.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, () ->
                preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the name is written in Cyrillic result incorrect")
    void givenNameWrittenInCyrillic_whenCallResultsOfValidationMethod_shouldThrowValidationException() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .email("something@qwe.com")
                .firstName("Петр")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, () ->
                preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the passport number contains more than 6 numbers result incorrect")
    void givenPassportNumberContainsMoreThanSixNumbers_whenCallResultsOfValidationMethod_shouldThrowValidationException() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("12345346")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, () ->
                preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the passport number contains less than 6 numbers result incorrect")
    void givenPassportNumberContainsLessThanSixNumbers_whenCallResultsOfValidationMethod_shouldThrowValidationException() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("12")
                .passportSeries("1234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, () ->
                preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the passport series contains more than 4 numbers result incorrect")
    void givenPassportSeriesContainsMoreThanForNumbers_whenCallResultsOfValidationMethod_shouldThrowValidationException() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1231231234")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, () ->
                preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the passport series contains more less than 4 numbers result incorrect")
    void givenPassportSeriesContainsLessThanForNumbers_whenCallResultsOfValidationMethod_shouldThrowValidationException() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("12")
                .term(10)
                .build();
        Assertions.assertThrows(ValidationException.class, () ->
                preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the term less than 6 months result incorrect")
    void givenTermLessThanSix_whenCallResultsOfValidationMethod_shouldThrowValidationException() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(2)
                .build();
        Assertions.assertThrows(ValidationException.class, () ->
                preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the term bigger than 6 months result correct")
    void givenTermBiggerThanSix_whenCallResultsOfValidationMethod_shouldThrowValidationException() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(12)
                .build();
        Assertions.assertDoesNotThrow(() -> preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }

    @Test
    @DisplayName("the term equals 6 months result correct")
    void givenTermEqualsSix_whenCallResultsOfValidationMethod_shouldThrowValidationException() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .email("something@gmail.com")
                .firstName("Petr")
                .lastName("Petrov")
                .middleName("Petrovich")
                .passportNumber("123456")
                .passportSeries("1234")
                .term(6)
                .build();
        Assertions.assertDoesNotThrow(() -> preScoringValidationServiceIml.resultsOfValidation(loanStatementRequestDto));
    }
}