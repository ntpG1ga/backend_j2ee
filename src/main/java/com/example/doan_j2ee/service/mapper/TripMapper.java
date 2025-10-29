package com.example.doan_j2ee.service.mapper;

import com.example.doan_j2ee.dto.TripDTO;
import com.example.doan_j2ee.model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TripMapper {

    // ✅ Entity → DTO
    @Mapping(source = "bus.id", target = "busId")
    @Mapping(source = "route.routeId", target = "routeId")
    TripDTO toDto(Trip trip);

    // ✅ DTO → Entity
    // Vì bạn xử lý gán Bus và Route trong Service, nên bỏ qua khi map
    @Mapping(target = "bus", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "id", ignore = true)
    Trip toEntity(TripDTO dto);
}
 