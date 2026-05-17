package com.gamestore.restapis.mappers;

import com.gamestore.restapis.dtos.CategoryDto;
import com.gamestore.restapis.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @Mapping(target = "products", ignore = true)
    Category toEntity(CategoryDto categoryDto);

    @Mapping(target = "products", ignore = true)
    void update(CategoryDto categoryDto, @MappingTarget Category category);
}
