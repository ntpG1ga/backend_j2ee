package com.example.doan_j2ee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;

// ----------------------------------------------------

// ----------------------------------------------------

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteDTO {

    private Long routeId;

    @NotBlank(message = "Địa điểm bắt đầu không được để trống")
    private String startLocation;

    @NotBlank(message = "Địa điểm kết thúc không được để trống")
    private String endLocation;

    @NotNull(message = "Khoảng cách là bắt buộc")
    @Positive(message = "Khoảng cách phải là số dương")
    private Double distanceKm;

    @NotBlank(message = "Thời gian ước tính không được để trống")
    private String estimatedTime;
    // Constructors
    public RouteDTO() {}

    // Getters và Setters
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
