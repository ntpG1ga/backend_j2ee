package com.example.doan_j2ee.service;

import com.example.doan_j2ee.dto.BusDTO;
import com.example.doan_j2ee.model.Bus;
import com.example.doan_j2ee.repository.BusRepository;
import com.example.doan_j2ee.service.mapper.BusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusService {

    private final BusRepository busRepository;
    private final BusMapper busMapper;

    @Autowired
    public BusService(BusRepository busRepository, BusMapper busMapper) {
        this.busRepository = busRepository;
        this.busMapper = busMapper;
    }

    // --- 1. CREATE (Thêm Bus mới) ---
    public BusDTO createNewBus(BusDTO request) {
        // Kiểm tra một số logic cơ bản
        if (request.getLicensePlate() == null || request.getLicensePlate().isEmpty()) {
            throw new IllegalArgumentException("Biển số xe không được để trống.");
        }
        if (busRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new IllegalArgumentException("Biển số xe đã tồn tại.");
        }

        Bus bus = busMapper.toEntity(request);

        Bus savedBus = busRepository.save(bus);
        return busMapper.toDto(savedBus);
    }

    // --- 2. READ ALL (Lấy tất cả Buses) ---
    public List<BusDTO> findAllBuses() {
        List<Bus> buses = busRepository.findAll();
        return buses.stream()
                .map(busMapper::toDto)
                .collect(Collectors.toList());
    }

    // --- 3. READ ONE (Lấy Bus theo ID) ---
    public BusDTO findBusById(Long busId) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new RuntimeException("Bus not found with id: " + busId));
        return busMapper.toDto(bus);
    }

    // --- 4. UPDATE (Cập nhật Bus) ---
    public BusDTO updateBus(BusDTO request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("ID Bus không được để trống khi cập nhật.");
        }

        Bus existingBus = busRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Bus not found with id: " + request.getId()));

        // Kiểm tra licencePlate nếu thay đổi
        if (!existingBus.getLicensePlate().equals(request.getLicensePlate())) {
            if (busRepository.existsByLicensePlate(request.getLicensePlate())) {
                throw new IllegalArgumentException("Biển số xe mới đã tồn tại.");
            }
            existingBus.setLicensePlate(request.getLicensePlate());
        }

        existingBus.setBusType(request.getBusType());
        existingBus.setCapacity(request.getCapacity());

        Bus updatedBus = busRepository.save(existingBus);
        return busMapper.toDto(updatedBus);
    }

    // --- 5. DELETE (Xóa Bus) ---
    public void deleteBus(Long busId) {
        if (!busRepository.existsById(busId)) {
            throw new RuntimeException("Bus not found with id: " + busId);
        }
        busRepository.deleteById(busId);
    }
}
