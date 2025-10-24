package com.example.doan_j2ee.repository;

import com.example.doan_j2ee.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Tìm kiếm User theo Email (dùng cho đăng nhập và kiểm tra trùng lặp)
    Optional<User> findByEmail(String email);

    // Kiểm tra sự tồn tại của Email
    boolean existsByEmail(String email);
}