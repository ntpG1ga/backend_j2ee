package com.example.doan_j2ee.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email; // THAY THẾ: từ javax.validation.constraints.* sang jakarta.validation.constraints.*
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long userId; 
    
    @NotBlank(message = "Tên đầy đủ không được để trống")
    private String fullName;
    
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    private String phoneNumber;

    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password; 
    
    private String role; 
    private LocalDateTime createdAt; 

    // Constructors, Getters và Setters... (Không thay đổi logic)
    public UserDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}