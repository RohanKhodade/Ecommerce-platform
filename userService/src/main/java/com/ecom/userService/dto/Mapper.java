package com.ecom.userService.dto;

import com.ecom.userService.dto.request.AddAddressRequest;
import com.ecom.userService.dto.request.RegisterUserRequest;
import com.ecom.userService.dto.request.UpdateAddressRequest;
import com.ecom.userService.dto.response.AddressResponse;
import com.ecom.userService.dto.response.UserResponse;
import com.ecom.userService.entity.Address;
import com.ecom.userService.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Mapper {

    public User registerUserRequestToUser(RegisterUserRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setBirthdate(LocalDate.parse(request.getBirthdate()));
        user.setPhone(request.getPhone());
        return user;
    }

    public UserResponse UserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId().toString());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setBirthdate(user.getBirthdate().toString());
        return userResponse;
    }

    public Address AddressReqToAddress(AddAddressRequest request) {

        Address address = new Address();
        address.setBuilding(request.getBuilding());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setZip(request.getZip());
        return address;
    }

    public AddressResponse AddressToResponse(Address address) {
        AddressResponse response = new AddressResponse();
        response.setId(address.getId().toString());
        response.setBuilding(address.getBuilding());
        response.setStreet(address.getStreet());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setCountry(address.getCountry());
        response.setZip(address.getZip());
        return response;
    }
}