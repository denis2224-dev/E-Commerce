package com.gamestore.restapis.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    private Long id;
    private String street;
    private String city;
    private String zip;
    private String state;
    private Long userId;
}
