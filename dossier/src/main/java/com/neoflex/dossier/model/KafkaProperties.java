package com.neoflex.dossier.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties("kafka-properties")
public class KafkaProperties {
    private String kafkaServer;
    private String kafkaGroupId;
}
