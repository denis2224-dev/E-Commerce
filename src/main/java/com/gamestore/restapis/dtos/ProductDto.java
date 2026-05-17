package com.gamestore.restapis.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String description;
    private String name;
    private BigDecimal price;
    private Byte categoryId;
}
