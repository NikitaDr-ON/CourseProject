package com.neoflex.deal.service;

import com.neoflex.deal.dto.EmailMessage;
import com.neoflex.deal.enums.Theme;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface FillEmailMessageDtoService {

    EmailMessage fillEmailMessageDto(UUID statementId, Theme theme);

}
