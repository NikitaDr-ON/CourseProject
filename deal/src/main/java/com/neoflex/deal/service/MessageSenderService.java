package com.neoflex.deal.service;

import com.neoflex.deal.dto.EmailMessage;

public interface MessageSenderService {

    void sendRequests(EmailMessage emailMessage);
}
