
// ============================================
// ChatRequest.java
// ============================================
package com.example.doan_j2ee.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    
    @NotBlank(message = "Câu hỏi không được để trống")
    private String question;
    
    // Optional: có thể thêm để track user
    private String userId;
    
    // Optional: có thể thêm để track conversation
    private String sessionId;
}