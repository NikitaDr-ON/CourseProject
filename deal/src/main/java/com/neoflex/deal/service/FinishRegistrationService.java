package com.neoflex.deal.service;

import com.neoflex.deal.dto.FinishRegistrationRequestDto;

public interface FinishRegistrationService {
    void finishRegistration(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId);
}
