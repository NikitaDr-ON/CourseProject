package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.CreditDto;
import com.neoflex.CourseProject.dto.PaymentScheduleElementDto;
import com.neoflex.CourseProject.model.ScoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@Slf4j
public class CalculationServiceImpl implements CalculationService {

    private final ScoringProperties scoringProperties;

    @Autowired
    public CalculationServiceImpl(ScoringProperties scoringProperties) {
        this.scoringProperties = scoringProperties;
    }

    /**
     * {@link CalculationServiceImpl}
     *
     * @param creditDto используется для получения суммы кредита, которая используется в расчете пск и создании
     *                  расписания платежей
     *                  <p>Изначально остаток равен сумме запрошенного кредита. debtPayment считается как
     *                  остаток * ставку в % * количество дней в месяце / количество дней в году
     *                  basePayment рассчитывается как сумма запрошенного кредита / срок кредита
     *                  в месяцах.
     *                  totalPayment это сумма debtPayment и basePayment выплаченная за месяц.
     *                  В цикле заполняется расписание платежей по кредиту PaymentScheduleElementDto.
     *                  amountPayment сумма платежей за все время. В последней итерации конечный остаток по кредиту
     *                  прибавляется к базовому платежу, после чего остаток становится равным нулю</p>
     */
    @Override
    public void calculationOfPayments(CreditDto creditDto) {
        log.info("creditDto входные параметры: {}", creditDto);
        BigDecimal remainder = creditDto.getAmount();
        BigDecimal basePayment = calculatingBasePayment(creditDto.getAmount(), creditDto.getTerm());
        BigDecimal debtPayment;
        BigDecimal totalPayment;
        BigDecimal daysInYear;
        BigDecimal percent = calculatingPercent(creditDto.getRate());
        BigDecimal amountPayment = BigDecimal.valueOf(0);
        int daysInMonth;
        List<PaymentScheduleElementDto> paymentScheduleElementDto = new ArrayList<>();
        for (int i = 0; i < creditDto.getTerm(); i++) {
            daysInMonth = getDaysInMonth(i);
            daysInYear = getDaysInYear(i);
            debtPayment = calculatingDebtPayment(percent, remainder, daysInMonth, daysInYear);
            totalPayment = calculatingTotalPayment(debtPayment, basePayment);
            amountPayment = calculatingAmountPayment(amountPayment, totalPayment);
            if (i == creditDto.getTerm() - 1) {
                debtPayment = calculatingDebtPayment(percent, remainder, daysInMonth, daysInYear).add(
                        calculatingRemainder(remainder, basePayment));
                remainder = calculatingRemainder(remainder, basePayment).subtract(
                        calculatingRemainder(remainder, basePayment));
                log.info("остаток равен нулю");
            } else {
                remainder = remainder.subtract(basePayment);
            }
            paymentScheduleElementDto.add(fillOutTheSchedule(i, basePayment, debtPayment, totalPayment, remainder));
        }
        creditDto.setPsk(amountPayment);
        creditDto.setPaymentSchedule(paymentScheduleElementDto);
        creditDto.setMonthlyPayment(basePayment);
        log.info("расчет выплат завершен");
    }

    private BigDecimal calculatingBasePayment(BigDecimal amount, int term) {
        log.info("calculatingBasePayment входные параметры: {},{}", amount, term);
        BigDecimal basePayment = amount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
        log.info("результат расчета базового платежа: {}", basePayment);
        return basePayment;
    }

    private BigDecimal calculatingPercent(BigDecimal rate) {
        log.info("calculatingPercent входной параметр: {}", rate);
        BigDecimal percent = rate.divide(scoringProperties.getPercent(), 2, RoundingMode.HALF_UP);
        log.info("результат расчета процента: {}", percent);
        return percent;
    }

    private int getDaysInMonth(int i) {
        log.info("getDaysInMonth входные параметры: {}", i);
        int countOfDays = LocalDate.now().plusMonths(i).lengthOfMonth();
        log.info("количество дней в месяце {}", countOfDays);
        return countOfDays;
    }

    private BigDecimal getDaysInYear(int i) {
        log.info("getDaysInYear входные параметры: {}", i);
        BigDecimal daysInYear;
        GregorianCalendar calendar = new GregorianCalendar(LocalDate.now().plusMonths(i).getYear(),
                LocalDate.now().plusMonths(i).getMonthValue(), LocalDate.now().plusMonths(i).getDayOfMonth());
        if (calendar.isLeapYear(LocalDate.now().plusMonths(i).getYear())) {
            daysInYear = scoringProperties.getDaysInLeapYear();
        } else {
            daysInYear = scoringProperties.getDaysInYear();
        }
        log.info("количество дней в году {}", daysInYear);
        return daysInYear;
    }

    private BigDecimal calculatingDebtPayment(BigDecimal percent, BigDecimal remainder, int daysInMonth,
                                              BigDecimal daysInYear) {
        log.info("calculatingDebtPayment входные параметры: {}, {}, {}, {}",
                percent, remainder, daysInMonth, daysInYear);
        BigDecimal debtPayment = remainder.multiply(percent).multiply(BigDecimal.valueOf(daysInMonth)).
                divide(daysInYear, 2, RoundingMode.HALF_UP);
        log.info("результат расчета платежа по процентам: {}", debtPayment);
        return debtPayment;
    }

    private BigDecimal calculatingTotalPayment(BigDecimal debtPayment, BigDecimal basePayment) {
        log.info("calculatingTotalPayment входные параметры: {},{}", debtPayment, basePayment);
        BigDecimal totalPayment = basePayment.add(debtPayment);
        log.info("результат расчета полной оплаты за месяц: {}", totalPayment);
        return totalPayment;
    }

    private BigDecimal calculatingRemainder(BigDecimal remainder, BigDecimal basePayment) {
        log.info("calculatingRemainder входные параметры: {},{}", remainder, basePayment);
        BigDecimal currentRemainder = remainder.subtract(basePayment);
        log.info("результат расчета остатка по кредиту: {}", currentRemainder);
        return currentRemainder;
    }

    private BigDecimal calculatingAmountPayment(BigDecimal amountPayment, BigDecimal totalPayment) {
        log.info("calculatingAmountPayment входные параметры: {},{}", amountPayment, totalPayment);
        BigDecimal currentAmountPayment = amountPayment.add(totalPayment);
        log.info("результат расчета общей выплаты: {}", currentAmountPayment);
        return currentAmountPayment;
    }

    private PaymentScheduleElementDto fillOutTheSchedule(int i, BigDecimal basePayment, BigDecimal debtPayment,
                                                         BigDecimal totalPayment, BigDecimal remainder) {
        log.info("fillOutTheSchedule входные параметры: {},{},{},{},{}", i, basePayment, debtPayment, totalPayment,
                remainder);
        PaymentScheduleElementDto scheduleElement = PaymentScheduleElementDto.builder()
                .number(i)
                .date(LocalDate.now().plusMonths(i))
                .interestPayment(basePayment)
                .debtPayment(debtPayment)
                .totalPayment(totalPayment)
                .remainingDebt(remainder)
                .build();
        log.info("заполненный элемент расписания платежей: {}", scheduleElement);
        return scheduleElement;
    }
}
