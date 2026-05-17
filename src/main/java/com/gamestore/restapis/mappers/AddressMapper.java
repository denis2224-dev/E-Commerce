package com.gamestore.restapis.mappers;

import com.gamestore.restapis.dtos.AddressDto;
import com.gamestore.restapis.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "userId", source = "user.id")
    AddressDto toDto(Address address);

    @Mapping(target = "user.id", source = "userId")
    Address toEntity(AddressDto addressDto);

    @Mapping(target = "user.id", source = "userId")
    void update(AddressDto addressDto, @MappingTarget Address address);
}
