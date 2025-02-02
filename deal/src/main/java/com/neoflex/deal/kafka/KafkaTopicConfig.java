package com.neoflex.deal.kafka;

import com.neoflex.deal.model.KafkaProperties;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaTopicConfig {

    private final KafkaProperties kafkaProperties;

    @Autowired
    public KafkaTopicConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getKafkaServer());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topicFinishRegistration() {
        return new NewTopic("FINISH_REGISTRATION", 1, (short) 1);
    }
}
