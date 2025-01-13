package com.neoflex.statement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "post-service")
public class PostServiceProperties {

    private final String dealStatementUrl;
    private final String dealOfferSelectUrl;

}
