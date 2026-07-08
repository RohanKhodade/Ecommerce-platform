package com.ecom.userService.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddAddressRequest {

    private String building;
    @NotBlank private String street;
    @NotBlank private String city;
    @NotBlank private String state;
    @NotBlank private String zip;
    @NotBlank private String country;
}
