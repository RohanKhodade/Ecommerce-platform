package com.ecom.userService.dto.response;

import lombok.Data;

@Data
public class AddressResponse {

    private String building;
    private String street;
    private String city;
    private String state;
    private String country;

}
