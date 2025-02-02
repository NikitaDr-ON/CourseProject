package com.neoflex.deal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "email-message")
public class EmailMessageProperties {
    Integer minRangeForRandom;
    Integer maxRangeForRandom;
}
