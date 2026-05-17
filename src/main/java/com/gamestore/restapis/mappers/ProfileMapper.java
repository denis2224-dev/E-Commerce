package com.gamestore.restapis.mappers;

import com.gamestore.restapis.dtos.ProfileDto;
import com.gamestore.restapis.entities.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mapping(target = "userId", source = "user.id")
    ProfileDto toDto(Profile profile);

    @Mapping(target = "user.id", source = "userId")
    Profile toEntity(ProfileDto profileDto);

    @Mapping(target = "user.id", source = "userId")
    void update(ProfileDto profileDto, @MappingTarget Profile profile);
}
