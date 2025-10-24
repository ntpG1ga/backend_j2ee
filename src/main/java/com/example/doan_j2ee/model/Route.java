package com.example.doan_j2ee.model;

import jakarta.persistence.*;

@Entity
@Table(name = "routes") // Tên bảng trong cơ sở dữ liệu (ví dụ: route hoặc routes)
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "start_location", nullable = false)
    private String startLocation;

    @Column(name = "end_location", nullable = false)
    private String endLocation;

    @Column(name = "distance_km", nullable = false)
    private Double distanceKm;

    // Giả sử estimated_time được lưu dưới dạng chuỗi (TIME trong SQL)
    @Column(name = "estimated_time", nullable = false)
    private String estimatedTime; 

    // Constructors
    public Route() {
    }

    // Getters and Setters

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}
