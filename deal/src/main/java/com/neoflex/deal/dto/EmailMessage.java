package com.neoflex.deal.dto;

import com.neoflex.deal.enums.Theme;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EmailMessage {

    @Schema(description = "адрес почты клиента", example = "nikitokdr@gmail.com")
    private String address;
    @Schema(description = "тема топика kafka", example = "FINISH_REGISTRATION")
    private Theme theme;
    @Schema(description = "id заявки", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID statementId;
    @Schema(description = "текст письма", example = "Завершите регистрацию")
    private String text;

}
