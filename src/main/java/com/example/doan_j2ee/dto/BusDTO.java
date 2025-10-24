package com.example.doan_j2ee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusDTO {

    private Long id; // ID của xe

    @NotBlank(message = "Biển số không được để trống")
    private String licensePlate; // Biển số xe

    @NotBlank(message = "Loại xe không được để trống")
    private String busType; // Loại xe

    @NotNull(message = "Sức chứa là bắt buộc")
    @Positive(message = "Sức chứa phải là số dương")
    private Integer capacity; // Sức chứa hành khách

    // Constructors
    public BusDTO() {}

    public BusDTO(Long id, String licensePlate, String busType, Integer capacity) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.busType = busType;
        this.capacity = capacity;
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
