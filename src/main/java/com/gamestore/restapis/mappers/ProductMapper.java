package com.gamestore.restapis.mappers;

import com.gamestore.restapis.dtos.ProductDto;
import com.gamestore.restapis.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);

    @Mapping(target = "category.id", source = "categoryId")
    Product toEntity(ProductDto productDto);
}
