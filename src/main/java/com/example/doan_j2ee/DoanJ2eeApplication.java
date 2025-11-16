package com.example.doan_j2ee;

import com.example.doan_j2ee.config.EnvConfig;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(EnvConfig.class)  
public class DoanJ2eeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoanJ2eeApplication.class, args);
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}