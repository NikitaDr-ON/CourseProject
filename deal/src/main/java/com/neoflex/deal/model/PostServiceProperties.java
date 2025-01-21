package com.neoflex.deal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "post-service")
public class PostServiceProperties {

    private final String calculatorCalc;
    private final String calculatorOffers;

}
