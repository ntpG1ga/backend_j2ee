package com.example.doan_j2ee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    
    private boolean success;
    private String answer;
    private String error;
    private LocalDateTime timestamp;

    // Constructor helpers
    public static ChatResponse success(String answer) {
        return new ChatResponse(true, answer, null, LocalDateTime.now());
    }

    public static ChatResponse error(String message, String errorDetails) {
        return new ChatResponse(false, message, errorDetails, LocalDateTime.now());
    }
}