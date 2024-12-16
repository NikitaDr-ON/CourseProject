package com.neoflex.deal.dto;

import com.neoflex.deal.enums.ChangeType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StatusHistory {
    private String status;
    private LocalDateTime time;
    private ChangeType changeType;
}
