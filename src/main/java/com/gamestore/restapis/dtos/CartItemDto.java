package com.gamestore.restapis.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private String productImageUrl;
    private Byte categoryId;
    private Integer quantity;
    private BigDecimal subtotal;
}
