package com.gamestore.restapis.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CategoryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Byte id;
    private String name;
}
