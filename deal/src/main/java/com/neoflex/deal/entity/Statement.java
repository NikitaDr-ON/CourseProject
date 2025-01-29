package com.neoflex.deal.entity;

import com.neoflex.deal.dto.AppliedOffer;
import com.neoflex.deal.dto.StatusHistory;
import com.neoflex.deal.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
@Table(name = "statement")
public class Statement {

    @Id
    @GeneratedValue
    @Column(name = "statement_id")
    private UUID statementId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private Client client;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id", referencedColumnName = "credit_id")
    private Credit credit;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "applied_offer", columnDefinition = "jsonb")
    private AppliedOffer appliedOffer;

    @Column(name = "sign_date")
    private LocalDateTime signDate;

    @Column(name = "ses_code")
    private Integer sesCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "status_history", columnDefinition = "jsonb")
    private List<StatusHistory> statusHistory;
}
