package com.example.doan_j2ee.service.mapper;

import com.example.doan_j2ee.dto.RouteDTO;
import com.example.doan_j2ee.model.Route;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring") 
public interface RouteMapper { 
    
    /**
     * Chuyển đổi từ Entity Route sang DTO RouteDTO (cho Response)
     * Các trường đều được map tự động do tên khớp nhau (routeId, startLocation, v.v.)
     */
    RouteDTO toDto(Route route);
    
    /**
     * Chuyển đổi từ DTO RouteDTO sang Entity Route (cho Create/Update)
     * MapStruct sẽ tự động bỏ qua các trường không tồn tại trong Entity (ví dụ: các trường chỉ có trong DTO)
     */
    Route toEntity(RouteDTO routeDTO);

    /**
     * Chuyển đổi danh sách Entity sang danh sách DTO
     */
    List<RouteDTO> toDto(List<Route> routes);
}
