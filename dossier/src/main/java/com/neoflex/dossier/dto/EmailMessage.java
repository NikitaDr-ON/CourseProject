package com.neoflex.dossier.dto;

import com.neoflex.dossier.enums.Theme;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailMessage {

    private String address;
    private Theme theme;
    private Long statementId;
    private String text;

}
