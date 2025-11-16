package com.example.doan_j2ee.service;

import com.example.doan_j2ee.model.Route;
import com.example.doan_j2ee.model.Trip;
import com.example.doan_j2ee.repository.RouteRepository;
import com.example.doan_j2ee.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;

    /**
     * Xử lý tin nhắn từ user và trả về câu trả lời
     */
    public String chat(String userMessage) {
        // 1. Lấy context từ Vector Database (RAG)
        String vectorContext = getVectorContext(userMessage);
        
        // 2. Lấy dữ liệu thời gian thực từ Database
        String databaseInfo = getDatabaseInfo(userMessage);
        
        // 3. Tạo prompt và gọi LLM
        String prompt = buildPrompt(userMessage, vectorContext, databaseInfo);
        
        // 4. Gọi Ollama qua Spring AI
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    /**
     * Lấy context từ Vector Database
     */
    private String getVectorContext(String query) {
        try {
            List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.builder()
                    .query(query)
                    .topK(3)
                    .build()
            );
            
            return docs.stream()
                .map(Document::toString)
                .collect(Collectors.joining("\n\n"));
        } catch (Exception e) {
            System.err.println("Lỗi khi truy vấn Vector Store: " + e.getMessage());
            return "";
        }
    }

    /**
     * Lấy dữ liệu thời gian thực từ Database
     */
    private String getDatabaseInfo(String message) {
        StringBuilder info = new StringBuilder();
        String lower = message.toLowerCase();
        
        // Nếu hỏi về tuyến đường
        if (lower.contains("tuyến") || lower.contains("đường") || 
            lower.contains("từ") || lower.contains("đến")) {
            info.append(getRouteInfo());
        }
        
        // Nếu hỏi về chuyến xe
        if (lower.contains("chuyến") || lower.contains("xe") || 
            lower.contains("giờ") || lower.contains("khởi hành")) {
            info.append(getTripInfo());
        }
        
        // Nếu hỏi về giá
        if (lower.contains("giá") || lower.contains("tiền") || 
            lower.contains("bao nhiêu")) {
            info.append(getPriceInfo());
        }
        
        return info.toString();
    }

    /**
     * Lấy thông tin tuyến đường
     */
    private String getRouteInfo() {
        List<Route> routes = routeRepository.findAll();
        
        if (routes.isEmpty()) {
            return "Hiện tại chưa có tuyến đường nào.\n\n";
        }
        
        StringBuilder info = new StringBuilder("📍 CÁC TUYẾN ĐƯỜNG HIỆN CÓ:\n");
        for (Route route : routes) {
            info.append(String.format(
                "• %s → %s: %.0f km (Thời gian: %s)\n",
                route.getStartLocation(),
                route.getEndLocation(),
                route.getDistanceKm(),
                route.getEstimatedTime()
            ));
        }
        info.append("\n");
        
        return info.toString();
    }

    /**
     * Lấy thông tin chuyến xe
     */
    private String getTripInfo() {
        List<Trip> trips = tripRepository.findAll();
        
        if (trips.isEmpty()) {
            return "Hiện tại chưa có chuyến xe nào.\n\n";
        }
        
        // Lấy tối đa 10 chuyến
        trips = trips.stream().limit(10).collect(Collectors.toList());
        
        StringBuilder info = new StringBuilder("🚌 CÁC CHUYẾN XE:\n");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        for (Trip trip : trips) {
            info.append(String.format(
                "• %s → %s | Giờ đi: %s | Giá: %,.0fđ | Xe %s (%d chỗ) | Trạng thái: %s\n",
                trip.getRoute().getStartLocation(),
                trip.getRoute().getEndLocation(),
                trip.getDepartureTime().format(timeFormatter),
                trip.getPrice(),
                trip.getBus().getBusType(),
                trip.getBus().getCapacity(),
                trip.getStatus()
            ));
        }
        info.append("\n");
        
        return info.toString();
    }

    /**
     * Lấy thông tin giá vé
     */
    private String getPriceInfo() {
        List<Trip> trips = tripRepository.findAll();
        
        if (trips.isEmpty()) {
            return "Chưa có thông tin giá vé.\n\n";
        }
        
        // Nhóm theo tuyến và tính giá min/max
        var priceByRoute = trips.stream()
            .collect(Collectors.groupingBy(
                trip -> trip.getRoute().getStartLocation() + " - " + trip.getRoute().getEndLocation(),
                Collectors.summarizingDouble(Trip::getPrice)
            ));
        
        StringBuilder info = new StringBuilder("💰 BẢNG GIÁ VÉ:\n");
        priceByRoute.forEach((routeName, stats) -> {
            if (stats.getMin() == stats.getMax()) {
                info.append(String.format("• %s: %,.0fđ\n", routeName, stats.getMin()));
            } else {
                info.append(String.format("• %s: %,.0fđ - %,.0fđ\n", 
                    routeName, stats.getMin(), stats.getMax()));
            }
        });
        info.append("\n");
        
        return info.toString();
    }

    /**
     * Build prompt cho LLM
     */
    private String buildPrompt(String userMessage, String vectorContext, String databaseInfo) {
        return String.format("""
            Bạn là trợ lý ảo thông minh của hệ thống đặt vé xe khách.
            
            📚 THÔNG TIN TỪ TÀI LIỆU (KNOWLEDGE BASE):
            %s
            
            💾 DỮ LIỆU THỜI GIAN THỰC (DATABASE):
            %s
            
            📝 CÂU HỎI CỦA KHÁCH HÀNG:
            %s
            
            ⚡ HƯỚNG DẪN TRẢ LỜI:
            - Ưu tiên sử dụng dữ liệu thời gian thực từ database
            - Trả lời ngắn gọn, rõ ràng bằng tiếng Việt
            - Sử dụng emoji để câu trả lời sinh động
            - Format số tiền: 1.000.000đ
            - Format giờ: 08:00
            - Nếu không có thông tin, đề xuất khách gọi: Hotline 1900 1234
            - Kết thúc bằng câu hỏi "Bạn cần hỗ trợ gì thêm không?"
            
            Hãy trả lời:
            """,
            vectorContext.isEmpty() ? "Không có thông tin từ tài liệu." : vectorContext,
            databaseInfo.isEmpty() ? "Không có dữ liệu từ database." : databaseInfo,
            userMessage
        );
    }
}