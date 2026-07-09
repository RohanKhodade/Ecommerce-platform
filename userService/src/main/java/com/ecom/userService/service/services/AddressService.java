package com.ecom.userService.service.services;


import com.ecom.userService.dto.request.AddAddressRequest;
import com.ecom.userService.dto.request.UpdateAddressRequest;
import com.ecom.userService.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse createAddress(AddAddressRequest request,Long userId);
    AddressResponse updateAddress(Long userId,Long addressId,UpdateAddressRequest request);
    String deleteAddress(Long userId,Long addressId);
    AddressResponse getAddress(Long userId,Long addressId);
    List<AddressResponse> getAllAddresses(Long userId);
}
