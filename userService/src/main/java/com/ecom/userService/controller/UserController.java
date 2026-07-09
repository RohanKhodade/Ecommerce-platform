package com.ecom.userService.controller;

import com.ecom.userService.dto.request.AddAddressRequest;
import com.ecom.userService.dto.request.UpdateAddressRequest;
import com.ecom.userService.dto.request.UpdateUserRequest;
import com.ecom.userService.dto.response.AddressResponse;
import com.ecom.userService.dto.response.UserResponse;
import com.ecom.userService.service.services.AddressService;
import com.ecom.userService.service.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;
    public UserController(UserService userService,
                          AddressService addressService) {
        this.userService=userService;
        this.addressService=addressService;
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId,
                                                   @RequestBody UpdateUserRequest request){
        return new ResponseEntity<>(userService.updateUserDetails(userId, request), HttpStatus.OK);
    }

    @PostMapping("/address/create/{userId}")
    public ResponseEntity<AddressResponse> createAddress(@PathVariable Long userId,
                                                         @RequestBody AddAddressRequest request){
        return new ResponseEntity<>(addressService.createAddress(request,userId),HttpStatus.CREATED);
    }

    @PutMapping("/address/update/{userId}/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long userId,
                                                         @PathVariable Long addressId,
                                                         @RequestBody UpdateAddressRequest request){
        return new ResponseEntity<> (addressService.updateAddress(userId, addressId, request),
                HttpStatus.OK);
    }
    @GetMapping("/address/get/{userId}/{addressId}")
    public ResponseEntity<AddressResponse> getAddress(@PathVariable Long userId,
                                                      @PathVariable Long addressId){
        return new ResponseEntity<> (addressService.getAddress(userId,addressId),HttpStatus.OK);
    }

    @GetMapping("/address/getAll/{userId}")
    public ResponseEntity<List<AddressResponse>> getAddAddresses(@PathVariable Long userId){
        return new ResponseEntity<> (addressService.getAllAddresses(userId),HttpStatus.OK);
    }

    @DeleteMapping("/address/delete/{userId}/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long userId,
                                                @PathVariable Long addressId){
        return new ResponseEntity<> (addressService.deleteAddress(userId,addressId),HttpStatus.OK);
    }
}
