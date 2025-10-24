package com.example.doan_j2ee.service.mapper;

import com.example.doan_j2ee.dto.BusDTO;
import com.example.doan_j2ee.model.Bus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BusMapper {

    // Chuyển từ Entity sang DTO
    BusDTO toDto(Bus bus);

    // Chuyển từ DTO sang Entity
    // Trường hệ thống (id) sẽ được xử lý trong Service
    @Mapping(target = "id", ignore = true)
    Bus toEntity(BusDTO busDTO);
}
