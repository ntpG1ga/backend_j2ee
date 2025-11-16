package com.example.doan_j2ee.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final VectorStore vectorStore;

    @Override
    public void run(String... args) {
        log.info("BẮT ĐẦU KIỂM TRA VÀ THÊM DỮ LIỆU VÀO PINECONE...");

        try {
            // Kiểm tra xem đã có dữ liệu chưa (dùng query đơn giản)
            List<Document> existing = vectorStore.similaritySearch(
                SearchRequest.builder()
                    .query("chính sách hủy vé")
                    .topK(1)
                    .build()
            );

            if (existing.isEmpty()) {
                List<Document> documents = createKnowledgeBase();
                vectorStore.add(documents);
                log.info("ĐÃ THÊM {} TÀI LIỆU VÀO PINECONE", documents.size());
            } else {
                log.info("DỮ LIỆU ĐÃ TỒN TẠI TRONG PINECONE ({} tài liệu mẫu)", existing.size());
            }

        } catch (Exception e) {
            log.warn("KHÔNG THỂ KẾT NỐI ĐẾN PINECONE KHI KHỞI ĐỘNG: {}", e.getMessage());
            log.debug("Chi tiết lỗi: ", e);
            // Không dừng ứng dụng – backend vẫn chạy bình thường!
        }
    }

    private List<Document> createKnowledgeBase() {
        List<Document> docs = new ArrayList<>();

        // 1. CHÍNH SÁCH HỦY VÉ
        docs.add(new Document(
            "CHÍNH SÁCH HỦY VÉ:\n" +
            "- Hủy trước 24 giờ: Hoàn 100% tiền vé.\n" +
            "- Hủy trước 12-24 giờ: Hoàn 70% tiền vé.\n" +
            "- Hủy trước 6-12 giờ: Hoàn 50% tiền vé.\n" +
            "- Hủy trong vòng 6 giờ: Không hoàn tiền.\n" +
            "Thời gian tính theo giờ khởi hành của chuyến xe.",
            Map.of("category", "policy", "type", "cancellation")
        ));

        // 2. CHÍNH SÁCH ĐỔI VÉ
        docs.add(new Document(
            "CHÍNH SÁCH ĐỔI VÉ:\n" +
            "- Đổi sang chuyến khác cùng tuyến: Miễn phí nếu còn chỗ (chỉ được đổi 1 lần).\n" +
            "- Đổi sang tuyến khác: Chịu phí 10% + chênh lệch giá vé nếu có.\n" +
            "- Chỉ được đổi 1 lần duy nhất, đổi lần 2 phải hủy vé cũ và đặt vé mới.",
            Map.of("category", "policy", "type", "change")
        ));

        // 3. YÊU CẦU KHI ĐẶT VÉ
        docs.add(new Document(
            "YÊU CẦU KHI ĐẶT VÉ:\n" +
            "- Cung cấp CMND/CCCD hoặc Passport khi đặt vé.\n" +
            "- Trẻ em dưới 6 tuổi: Miễn phí (không có ghế riêng).\n" +
            "- Trẻ em 6-12 tuổi: Giảm 25% giá vé.\n" +
            "- Phải mang CMND/CCCD khi lên xe để đối chiếu.",
            Map.of("category", "policy", "type", "booking_requirement")
        ));

        // 4. LOẠI XE VÀ DỊCH VỤ
        docs.add(new Document(
            "CÁC LOẠI XE:\n" +
            "1. Xe giường nằm: 22-30 giường, điều hòa, wifi, nước uống miễn phí. Giá cao hơn xe thường 30-50%.\n" +
            "2. Xe limousine: 9-18 chỗ, ghế massage, wifi cao cấp, đồ ăn nhẹ. Đón trả tận nhà trong nội thành.\n" +
            "3. Xe ghế ngồi: 45-50 chỗ, điều hòa, toilet. Giá rẻ nhất, phù hợp đường ngắn dưới 5 giờ.",
            Map.of("category", "service", "type", "bus_types")
        ));

        // 5. PHƯƠNG THỨC THANH TOÁN
        docs.add(new Document(
            "PHƯƠNG THỨC THANH TOÁN:\n" +
            "- Thanh toán online: VNPAY, MoMo, ZaloPay, VietQR. Giảm 5% khi thanh toán online.\n" +
            "- Thanh toán tại quầy: Tiền mặt hoặc thẻ.\n" +
            "- Thanh toán trên xe: Chỉ một số tuyến, phụ thu 10.000đ.",
            Map.of("category", "payment", "type", "methods")
        ));

        // 6. QUY ĐỊNH HÀNH LÝ
        docs.add(new Document(
            "QUY ĐỊNH HÀNH LÝ:\n" +
            "- Hành lý xách tay: Tối đa 7kg, kích thước 56x36x23cm.\n" +
            "- Hành lý ký gửi: Tối đa 20kg miễn phí. Vượt mức phụ thu 10.000đ/kg.\n" +
            "- Cấm mang: Chất lỏng dễ cháy, vũ khí, động vật sống (trừ chó dẫn đường).",
            Map.of("category", "policy", "type", "luggage")
        ));

        // 7. THÔNG TIN LIÊN HỆ
        docs.add(new Document(
            "THÔNG TIN LIÊN HỆ HỖ TRỢ:\n" +
            "- Hotline 24/7: 1900 1234\n" +
            "- Email: support@busbooking.vn\n" +
            "- Fanpage: facebook.com/busbooking\n" +
            "- Zalo: 0987654321\n" +
            "- Thời gian xử lý khiếu nại: 24-48 giờ.",
            Map.of("category", "contact", "type", "support")
        ));

        // 8. KHUYẾN MÃI
        docs.add(new Document(
            "CHƯƠNG TRÌNH KHUYẾN MÃI:\n" +
            "- Đặt vé lượt về trong vòng 7 ngày: Giảm 10%.\n" +
            "- Đặt từ 4 vé trở lên: Giảm 15%.\n" +
            "- Thành viên VIP: Tích điểm, đổi vé miễn phí.\n" +
            "- Sinh viên: Giảm 10% (xuất trình thẻ sinh viên).",
            Map.of("category", "promotion", "type", "discount")
        ));

        // 9. TUYẾN PHỔ BIẾN
        docs.add(new Document(
            "CÁC TUYẾN XE PHỔ BIẾN:\n" +
            "- Hà Nội - Sài Gòn: 1.700km, 30 giờ, giá 800.000đ - 1.500.000đ\n" +
            "- Hà Nội - Hải Phòng: 102km, 2 giờ, giá 100.000đ - 200.000đ\n" +
            "- TP.HCM - Vũng Tàu: 125km, 2.5 giờ, giá 120.000đ - 250.000đ\n" +
            "- TP.HCM - Đà Lạt: 308km, 7 giờ, giá 250.000đ - 450.000đ\n" +
            "- Đà Nẵng - Huế: 95km, 2.5 giờ, giá 150.000đ - 300.000đ",
            Map.of("category", "route", "type", "popular")
        ));

        // 10. GIỜ KHỞI HÀNH PHỔ BIẾN
        docs.add(new Document(
            "GIỜ KHỞI HÀNH THÔNG THƯỜNG:\n" +
            "- Buổi sáng: 6:00, 7:00, 8:00, 9:00\n" +
            "- Buổi trưa: 12:00, 13:00, 14:00\n" +
            "- Buổi chiều: 15:00, 16:00, 17:00, 18:00\n" +
            "- Buổi tối: 20:00, 21:00, 22:00, 23:00",
            Map.of("category", "schedule", "type", "departure_times")
        ));

        return docs;
    }
}