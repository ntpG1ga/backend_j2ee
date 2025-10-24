package com.example.doan_j2ee.service.mapper;

import com.example.doan_j2ee.dto.UserDTO;
import com.example.doan_j2ee.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") 

public interface UserMapper { 
 
    // Chuyển từ Entity sang DTO (cho Response)
    // Bỏ qua trường 'password' trong DTO (sẽ là null khi trả về)
    @Mapping(target = "password", ignore = true) 
    UserDTO toDto(User user);
    
    // Chuyển từ DTO sang Entity (cho Create/Update)
    // Các trường hệ thống (userId, passwordHash, role, createdAt) sẽ được xử lý trong Service
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "passwordHash", ignore = true) 
    @Mapping(target = "role", ignore = true) 
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserDTO userDTO);
}