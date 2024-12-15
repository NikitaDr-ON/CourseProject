package com.neoflex.deal.entity;

import com.neoflex.deal.enums.CreditStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "credit")
public class Credit {

    @Id
    @Column(name = "credit_id")
    private UUID creditId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "term")
    private int term;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "psk")
    private BigDecimal psk;

//    @Column(name = "payment_schedule")
//    private

    @Column(name = "insurance_enabled")
    private boolean insuranceEnabled;

    @Column(name = "salary_client")
    private boolean salaryClient;

    @Column(name = "credit_status")
    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;

    @OneToOne(mappedBy = "credit")
    private Statement statement;
}
