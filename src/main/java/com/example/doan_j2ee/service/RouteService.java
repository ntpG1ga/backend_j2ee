package com.example.doan_j2ee.service;

import com.example.doan_j2ee.dto.RouteDTO;
import com.example.doan_j2ee.model.Route;
import com.example.doan_j2ee.repository.RouteRepository;
import com.example.doan_j2ee.service.mapper.RouteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    @Autowired
    public RouteService(RouteRepository routeRepository, RouteMapper routeMapper) {
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
    }

    // --- 1. CREATE (Thêm Route) ---
    public RouteDTO createNewRoute(RouteDTO request) {
        // Có thể thêm logic kiểm tra trùng lặp tại đây nếu cần (ví dụ: trùng StartLocation/EndLocation)
        
        // 1. Chuyển DTO sang Entity
        Route route = routeMapper.toEntity(request);
        
        // 2. Lưu vào Database
        Route savedRoute = routeRepository.save(route);
        
        // 3. Chuyển Entity đã lưu sang DTO để trả về
        return routeMapper.toDto(savedRoute);
    }

    // --- 2. READ ALL (Lấy tất cả Routes) ---
    public List<RouteDTO> findAllRoutes() {
        List<Route> routes = routeRepository.findAll();
        // Sử dụng RouteMapper để chuyển List<Entity> sang List<DTO>
        return routeMapper.toDto(routes); 
        
        // Hoặc cách thủ công (như trong UserService):
        // return routes.stream()
        //         .map(routeMapper::toDto)
        //         .collect(Collectors.toList());
    }

    // --- 3. READ ONE (Lấy Route theo ID) ---
    public RouteDTO findRouteById(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found with id: " + routeId));
        
        return routeMapper.toDto(route);
    }

    // --- 4. UPDATE (Cập nhật Route) ---
    public RouteDTO updateRoute(RouteDTO request) {
        if (request.getRouteId() == null) {
            throw new IllegalArgumentException("Route ID không được để trống khi cập nhật.");
        }
        
        // 1. Tìm Entity Route hiện có
        Route existingRoute = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found with id: " + request.getRouteId()));

        // 2. Cập nhật các trường từ DTO request
        // Ta cập nhật từng trường để tránh ghi đè ID và đảm bảo tính an toàn
        existingRoute.setStartLocation(request.getStartLocation());
        existingRoute.setEndLocation(request.getEndLocation());
        existingRoute.setDistanceKm(request.getDistanceKm());
        existingRoute.setEstimatedTime(request.getEstimatedTime());

        // 3. Lưu và trả về
        Route updatedRoute = routeRepository.save(existingRoute);
        return routeMapper.toDto(updatedRoute);
    }

    // --- 5. DELETE (Xóa Route) ---
    public void deleteRoute(Long routeId) {
        if (!routeRepository.existsById(routeId)) {
            throw new RuntimeException("Route not found with id: " + routeId);
        }
        routeRepository.deleteById(routeId);
    }
}
