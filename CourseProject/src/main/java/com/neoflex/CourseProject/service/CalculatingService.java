package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.CreditDto;
import com.neoflex.CourseProject.dto.PaymentScheduleElementDto;
import com.neoflex.CourseProject.pojo.ScoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CalculatingService {

    private final ScoringProperties scoringProperties;

    @Autowired
    public CalculatingService(ScoringProperties scoringProperties){
        this.scoringProperties = scoringProperties;
    }

    /**
     * {@link com.neoflex.CourseProject.service.CalculatingService}
     * @param creditDto используется для получения суммы кредита, которая используется в расчете пск и создании расписания платежей
     * <p>Изначально остаток равен сумме запрошенного кредита. debetPayment считается как остаток * ставку в %
     *                  * количестово дней в месяце / количество дней в году(в расчете используется 366 дней)
     *                 basePayment расчитывается как сумма запрошенного кредита / срок кредита в месяцах.
     *                 totalPayment это сумма debetPayment и basePayment выплаченная за месяц.
     *                  В цикле заполняется расписание платежей по кредиту PaymentScheduleElementDto.
     *                  amountPayment сумма платежей за все вресмя</p>
     */
    public void calculationOfPayments(CreditDto creditDto){
        log.info("creditDto input: {}",creditDto);
        BigDecimal remainder = creditDto.getAmount();
        BigDecimal basePayment = (creditDto.getAmount().
                divide(BigDecimal.valueOf(creditDto.getTerm()),2, RoundingMode.HALF_UP));
        BigDecimal debetPayment;
        BigDecimal totalPayment = BigDecimal.valueOf(0);
        BigDecimal percent = (creditDto.getRate().divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP));
        BigDecimal amountPayment = BigDecimal.valueOf(0);
        List<PaymentScheduleElementDto> paymentScheduleElementDto = new ArrayList<>();
        for (int i=0;i<creditDto.getTerm();i++){
            int daysInMonth = LocalDate.now().plusMonths(i).lengthOfMonth();
            debetPayment = remainder.multiply(percent).multiply(BigDecimal.valueOf(daysInMonth)).
                    divide(scoringProperties.getDaysInYear(), 2, RoundingMode.HALF_UP);
            totalPayment = basePayment.add(debetPayment);
            amountPayment = amountPayment.add(totalPayment);
            paymentScheduleElementDto.add(PaymentScheduleElementDto.builder()
                    .number(i)
                    .date(LocalDate.now().plusMonths(i))
                    .interestPayment(basePayment)
                    .debtPayment(debetPayment)
                    .totalPayment(basePayment.add(debetPayment))
                    .remainingDebt(remainder)
                    .build());
            remainder = remainder.subtract(basePayment);
        }
        creditDto.setPsk(amountPayment);
        creditDto.setPaymentSchedule(paymentScheduleElementDto);
        creditDto.setMonthlyPayment(basePayment);
        log.info("calculation of payments is complete");
    }

}
