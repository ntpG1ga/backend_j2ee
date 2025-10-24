package com.example.doan_j2ee.repository;

import com.example.doan_j2ee.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {

    // Tìm kiếm Bus theo biển số (dùng để kiểm tra trùng lặp)
    Optional<Bus> findByLicensePlate(String licensePlate);

    // Kiểm tra sự tồn tại của biển số
    boolean existsByLicensePlate(String licensePlate);
}

