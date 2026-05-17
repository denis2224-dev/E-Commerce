package com.gamestore.restapis.mappers;

import com.gamestore.restapis.dtos.RegisterUserRequest;
import com.gamestore.restapis.dtos.UpdateUserRequest;
import com.gamestore.restapis.dtos.UserDto;
import com.gamestore.restapis.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request,@MappingTarget User user);
}
