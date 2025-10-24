package com.example.doan_j2ee.service;

import com.example.doan_j2ee.dto.UserDTO;
import com.example.doan_j2ee.model.User;
import com.example.doan_j2ee.repository.UserRepository;
import com.example.doan_j2ee.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // --- 1. CREATE (Đăng ký User) ---
    public UserDTO registerNewUser(UserDTO request) {
        // Kiểm tra logic validation thêm (vì DTO gộp không xử lý đủ)
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
             throw new IllegalArgumentException("Mật khẩu không được để trống khi tạo mới.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng.");
        }

        User user = userMapper.toEntity(request);
        
        // Mã hóa Mật khẩu (GIẢ LẬP)
        user.setPasswordHash(request.getPassword() + "_hashed"); 

        user.setRole("customer"); 
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    // --- 2. READ ALL (Lấy tất cả Users) ---
    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());
    }

    // --- 3. READ ONE (Lấy User theo ID) ---
    public UserDTO findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        return userMapper.toDto(user);
    }

    // --- 4. UPDATE (Cập nhật User) ---
    public UserDTO updateUser(UserDTO request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID không được để trống khi cập nhật.");
        }
        
        User existingUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        // Kiểm tra Email nếu thay đổi
        if (!existingUser.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email mới đã được sử dụng.");
            }
            existingUser.setEmail(request.getEmail());
        }

        existingUser.setFullName(request.getFullName());
        existingUser.setPhoneNumber(request.getPhoneNumber());
        
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    // --- 5. DELETE (Xóa User) ---
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }
}