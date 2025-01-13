package com.neoflex.deal.entity;

import com.neoflex.deal.dto.PaymentScheduleElementDto;
import com.neoflex.deal.enums.CreditStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    private Integer term;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "psk")
    private BigDecimal psk;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payment_schedule", columnDefinition = "jsonb")
    private PaymentScheduleElementDto paymentSchedule;

    @Column(name = "insurance_enabled")
    private Boolean insuranceEnabled;

    @Column(name = "salary_client")
    private Boolean salaryClient;

    @Column(name = "credit_status")
    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;

    @OneToOne(mappedBy = "credit")
    private Statement statement;
}
