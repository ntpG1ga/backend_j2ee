package com.example.doan_j2ee.repository;

import com.example.doan_j2ee.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    // Ví dụ về truy vấn tìm kiếm tùy chỉnh: Tìm kiếm Route theo điểm đi và điểm đến
    Optional<Route> findByStartLocationAndEndLocation(String startLocation, String endLocation);

    // Ví dụ: Kiểm tra sự tồn tại của Route dựa trên điểm đi và điểm đến (dùng cho validation trùng lặp)
    boolean existsByStartLocationAndEndLocation(String startLocation, String endLocation);
}
