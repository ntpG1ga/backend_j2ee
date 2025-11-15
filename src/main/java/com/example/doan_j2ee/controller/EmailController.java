// src/main/java/com/example/doan_j2ee/controller/EmailController.java
package com.example.doan_j2ee.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/email")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,##0");

    // === TEST EMAIL ===
    @PostMapping("/test")
    public ResponseEntity<String> sendTestEmail(@RequestBody String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email không được để trống");
            }

            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
            String html = new StringBuilder()
                .append("<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 20px auto; padding: 25px; border: 1px solid #eee; border-radius: 12px; background: #f9f9f9;\">")
                .append("<h3 style=\"color: #f37021; text-align: center;\">TEST EMAIL</h3>")
                .append("<p>Xin chào!</p>")
                .append("<p>Email này được gửi từ hệ thống đặt vé xe.</p>")
                .append("<p><strong>Backend đang hoạt động tốt!</strong></p>")
                .append("<p>Thời gian: ").append(time).append("</p>")
                .append("<hr>")
                .append("<p style=\"color: #888; font-size: 13px;\"><small>Email test tự động</small></p>")
                .append("</div>")
                .toString();

            sendHtmlEmail(email.trim(), "TEST EMAIL - HỆ THỐNG HOẠT ĐỘNG", html);
            return ResponseEntity.ok("Đã gửi email test đến: " + email.trim());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // === GỬI VÉ XE ===
    @PostMapping("/send-ticket")
    public ResponseEntity<String> sendTicket(@RequestBody Map<String, Object> data) {
        try {
            String email = (String) data.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email không được để trống");
            }

            String code = "TICKET-" + System.currentTimeMillis();
            byte[] qrBytes = generateQRCodeBytes(code);

            // Xử lý price
            int price = 0;
            Object priceObj = data.get("price");
            if (priceObj != null) {
                String priceStr = priceObj.toString().trim();
                if (!priceStr.isEmpty()) {
                    try {
                        price = Integer.parseInt(priceStr);
                    } catch (NumberFormatException e) {
                        return ResponseEntity.badRequest().body("Giá vé không hợp lệ: " + priceStr);
                    }
                }
            }
            String priceFormatted = PRICE_FORMAT.format(price) + " đ";

            // Lấy dữ liệu
            String from = getString(data, "from", "Không rõ");
            String to = getString(data, "to", "Không rõ");
            String time = getString(data, "time", "Không rõ");
            String seat = getString(data, "seat", "Không rõ");
            String name = getString(data, "name", "Khách hàng");
            String phone = getString(data, "phone", "Không có");

            // HTML
            String html = new StringBuilder()
                .append("<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 20px auto; padding: 25px; border: 1px solid #eee; border-radius: 12px; background: #f9f9f9; box-shadow: 0 2px 8px rgba(0,0,0,0.1);\">")
                .append("<h2 style=\"color: #f37021; text-align: center; margin-bottom: 20px;\">VÉ XE ĐÃ ĐẶT THÀNH CÔNG</h2>")
                .append("<hr style=\"border: 1px dashed #ddd;\">")
                .append("<table style=\"width: 100%; font-size: 15px; line-height: 1.8;\">")
                .append("<tr><td><strong>Mã vé:</strong></td><td><strong>").append(escapeHtml(code)).append("</strong></td></tr>")
                .append("<tr><td><strong>Tuyến:</strong></td><td>").append(escapeHtml(from)).append(" → ").append(escapeHtml(to)).append("</td></tr>")
                .append("<tr><td><strong>Giờ khởi hành:</strong></td><td>").append(escapeHtml(time)).append("</td></tr>")
                .append("<tr><td><strong>Ghế:</strong></td><td>").append(escapeHtml(seat)).append("</td></tr>")
                .append("<tr><td><strong>Giá vé:</strong></td><td>").append(priceFormatted).append("</td></tr>")
                .append("<tr><td><strong>Hành khách:</strong></td><td>").append(escapeHtml(name)).append("</td></tr>")
                .append("<tr><td><strong>SĐT:</strong></td><td>").append(escapeHtml(phone)).append("</td></tr>")
                .append("</table>")
                .append("<hr style=\"border: 1px dashed #ddd;\">")
                .append("<div style=\"text-align: center; margin: 25px 0;\">")
                .append("<img src=\"cid:qrCode\" width=\"180\" alt=\"QR Code Vé\" style=\"border: 2px solid #f37021; border-radius: 8px;\" />")
                .append("<p style=\"color: #666; margin-top: 10px; font-style: italic;\">Xuất trình QR này khi lên xe</p>")
                .append("</div>")
                .append("<p style=\"text-align: center; color: #888; font-size: 13px;\">")
                .append("Cảm ơn bạn đã sử dụng dịch vụ!<br>")
                .append("<small>Email được gửi tự động từ hệ thống đặt vé xe</small>")
                .append("</p>")
                .append("</div>")
                .toString();

            // GỬI EMAIL VỚI QR ĐÍNH KÈM
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email.trim());
            helper.setSubject("Vé xe - " + code);
            helper.setText(html, true);

            // SỬA LỖI: Dùng ByteArrayResource
            helper.addInline("qrCode", new ByteArrayResource(qrBytes), "image/png");

            mailSender.send(message);

            return ResponseEntity.ok("Đã gửi vé về email: " + email.trim());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // === THÊM METHOD sendHtmlEmail ===
    private void sendHtmlEmail(String to, String subject, String html) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        mailSender.send(message);
    }

    // === HỖ TRỢ ===
    private String getString(Map<String, Object> data, String key, String defaultValue) {
        Object value = data.get(key);
        if (value == null) return defaultValue;
        String str = value.toString().trim();
        return str.isEmpty() ? defaultValue : str;
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }

    private byte[] generateQRCodeBytes(String text) throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
        return baos.toByteArray();
    }
}