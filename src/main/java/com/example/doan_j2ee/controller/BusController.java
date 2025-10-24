package com.example.doan_j2ee.controller;

import com.example.doan_j2ee.dto.BusDTO;
import com.example.doan_j2ee.service.BusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buses")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class BusController {

    private final BusService busService;

    @Autowired
    public BusController(BusService busService) {
        this.busService = busService;
    }

    // --- 1. CREATE (Thêm Bus mới) ---
    @PostMapping
    public ResponseEntity<BusDTO> createBus(@Valid @RequestBody BusDTO request) {
        try {
            BusDTO newBus = busService.createNewBus(request);
            return new ResponseEntity<>(newBus, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // --- 2. READ ALL (Lấy tất cả Buses) ---
    @GetMapping
    public ResponseEntity<List<BusDTO>> getAllBuses() {
        List<BusDTO> buses = busService.findAllBuses();
        return ResponseEntity.ok(buses);
    }

    // --- 3. READ ONE (Lấy Bus theo ID) ---
    @GetMapping("/{id}")
    public ResponseEntity<BusDTO> getBusById(@PathVariable Long id) {
        try {
            BusDTO bus = busService.findBusById(id);
            return ResponseEntity.ok(bus);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- 4. UPDATE (Cập nhật Bus) ---
    @PutMapping
    public ResponseEntity<BusDTO> updateBus(@Valid @RequestBody BusDTO request) {
        try {
            BusDTO updatedBus = busService.updateBus(request);
            return ResponseEntity.ok(updatedBus);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- 5. DELETE (Xóa Bus) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        try {
            busService.deleteBus(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
