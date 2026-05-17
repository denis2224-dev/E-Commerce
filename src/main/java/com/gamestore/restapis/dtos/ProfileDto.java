package com.gamestore.restapis.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileDto {
    private Long id;
    private String bio;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Integer loyaltyPoints;
    private Long userId;
}
