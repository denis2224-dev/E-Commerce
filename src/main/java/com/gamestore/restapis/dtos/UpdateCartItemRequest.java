package com.gamestore.restapis.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartItemRequest {
    @Min(1)
    @Max(99)
    private Integer quantity;
}
