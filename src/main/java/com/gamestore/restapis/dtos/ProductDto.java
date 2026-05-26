package com.gamestore.restapis.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String description;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private Byte categoryId;
}
