package com.example.doan_j2ee.dto;

import java.time.LocalTime;

public class TripDTO {
    private Long id;
    private Long busId;
    private Long routeId;
    private LocalTime departureTime;   // ✅ dùng LocalTime
    private LocalTime arrivalTime;     // ✅ dùng LocalTime
    private Double price;
    private String status;

    public TripDTO() {}

    public TripDTO(Long id, Long busId, Long routeId, LocalTime departureTime, LocalTime arrivalTime, Double price, String status) {
        this.id = id;
        this.busId = busId;
        this.routeId = routeId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.status = status;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBusId() { return busId; }
    public void setBusId(Long busId) { this.busId = busId; }

    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }

    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime departureTime) { this.departureTime = departureTime; }

    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
