package com.example.doan_j2ee.controller;

import com.example.doan_j2ee.dto.RouteDTO;
import com.example.doan_j2ee.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routes")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class RouteController {

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // --- 1. CREATE (Thêm Route mới) ---
    @PostMapping
    public ResponseEntity<RouteDTO> createRoute(@Valid @RequestBody RouteDTO request) {
        try {
            RouteDTO newRoute = routeService.createNewRoute(request);
            return new ResponseEntity<>(newRoute, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // --- 2. READ ALL (Lấy tất cả Routes) ---
    @GetMapping
    public ResponseEntity<List<RouteDTO>> getAllRoutes() {
        List<RouteDTO> routes = routeService.findAllRoutes();
        return ResponseEntity.ok(routes);
    }

    // --- 3. READ ONE (Lấy Route theo ID) ---
    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO> getRouteById(@PathVariable Long id) {
        try {
            RouteDTO route = routeService.findRouteById(id);
            return ResponseEntity.ok(route);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- 4. UPDATE (Cập nhật Route) ---
    @PutMapping("/{id}")
public ResponseEntity<RouteDTO> updateRoute(
        @PathVariable Long id,
        @Valid @RequestBody RouteDTO request) {
    // Ép routeId từ path variable
    request.setRouteId(id);
    try {
        RouteDTO updatedRoute = routeService.updateRoute(request);
        return ResponseEntity.ok(updatedRoute);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}


    // --- 5. DELETE (Xóa Route) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        try {
            routeService.deleteRoute(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
