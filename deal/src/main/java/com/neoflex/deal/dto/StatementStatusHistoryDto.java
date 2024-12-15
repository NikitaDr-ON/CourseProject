package com.neoflex.deal.dto;

import com.neoflex.deal.enums.ApplicationStatus;
import com.neoflex.deal.enums.ChangeType;

import java.time.LocalDate;

public class StatementStatusHistoryDto {

    private ApplicationStatus status;
    private LocalDate time;
    private ChangeType changeType;

}
