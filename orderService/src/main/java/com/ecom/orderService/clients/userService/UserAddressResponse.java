package com.ecom.orderService.clients.userService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressResponse {
    private String id;
    private String building;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zip;
}
