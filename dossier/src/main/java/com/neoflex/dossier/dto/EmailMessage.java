package com.neoflex.dossier.dto;

import com.neoflex.dossier.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {

    private String address;
    private Theme theme;
    private Long statementId;
    private String text;

}
