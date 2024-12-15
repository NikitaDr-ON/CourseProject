package com.neoflex.deal.dto;

import com.neoflex.deal.enums.ChangeType;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class StatusHistory {
    private String status;
    private Timestamp time;
    private ChangeType changeType;
}
