package com.example.doan_j2ee.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    static {
        String projectDir = System.getProperty("user.dir");
        Dotenv dotenv = Dotenv.configure()
                .directory(projectDir)
                .filename(".env")
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry -> {
            if (System.getenv(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        });
        System.out.println("Đã load .env từ: " + projectDir + "/.env");
    }
}