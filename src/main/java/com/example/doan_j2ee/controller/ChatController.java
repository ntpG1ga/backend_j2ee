package com.example.doan_j2ee.controller;

import com.example.doan_j2ee.dto.ChatRequest;
import com.example.doan_j2ee.dto.ChatResponse;
import com.example.doan_j2ee.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:3000"})
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * Endpoint chính cho chatbot
     * POST /api/v1/chat
     * Body: { "question": "câu hỏi của user" }
     */
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        try {
            String answer = chatService.chat(request.getQuestion());
            return ResponseEntity.ok(ChatResponse.success(answer));
        } catch (Exception e) {
            System.err.println("Lỗi trong ChatController: " + e.getMessage());
            e.printStackTrace();
            
            return ResponseEntity.ok(ChatResponse.error(
                "Xin lỗi, hệ thống đang gặp sự cố. Vui lòng thử lại sau hoặc liên hệ: 1900 1234",
                e.getMessage()
            ));
        }
    }

    /**
     * Health check endpoint
     * GET /api/v1/chat/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("✅ Chatbot service đang hoạt động!");
    }
}