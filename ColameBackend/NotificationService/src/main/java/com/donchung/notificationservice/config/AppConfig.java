package com.donchung.notificationservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public NewTopic userTopic() {
        return new NewTopic("user", 2, (short) 1);
    }
}
