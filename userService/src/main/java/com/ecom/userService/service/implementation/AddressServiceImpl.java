package com.ecom.userService.service.implementation;

import com.ecom.userService.dto.Mapper;
import com.ecom.userService.dto.request.AddAddressRequest;
import com.ecom.userService.dto.request.UpdateAddressRequest;
import com.ecom.userService.dto.response.AddressResponse;
import com.ecom.userService.entity.Address;
import com.ecom.userService.entity.User;
import com.ecom.userService.exception.ResourceNotFoundException;
import com.ecom.userService.repository.AddressRepository;
import com.ecom.userService.repository.UserRepository;
import com.ecom.userService.service.services.AddressService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;
    public AddressServiceImpl(AddressRepository addressRepository,
                              UserRepository userRepository,
                              Mapper mapper) {
        this.addressRepository = addressRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }
    @Override
    public AddressResponse createAddress(AddAddressRequest request,Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("user not found"));
        Address address=mapper.AddressReqToAddress(request);
        address.setUser(user);
        return mapper.AddressToResponse(addressRepository.save(address));
    }

    @Override
    public AddressResponse updateAddress(Long userId,
                                         Long addressId,
                                         UpdateAddressRequest request) {
        Address address=addressRepository.findByIdAndUserId(addressId,userId).
                orElseThrow(()-> new ResourceNotFoundException(" Address not found with user: "+
                        userId+", address: "+ addressId));
        address.setBuilding(request.getBuilding());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setZip(request.getZip());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        addressRepository.save(address);
        return mapper.AddressToResponse(address);
    }

    @Override
    public String deleteAddress(Long userId, Long addressId) {
        if (!addressRepository.existsByIdAndUserId(addressId, userId)) {
            throw new ResourceNotFoundException("Address not found");
        }
        addressRepository.deleteById(addressId);
        return "Address Deleted";
    }

    @Override
    public AddressResponse getAddress(Long userId, Long addressId) {
        Address address=addressRepository.findByIdAndUserId(addressId, userId).orElseThrow(
                ()-> new ResourceNotFoundException(" Address not found with user: "+
                        userId+", address: "+ addressId)
        );
        return mapper.AddressToResponse(address);
    }

    @Override
    public List<AddressResponse> getAllAddresses(Long userId) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("user not found");
        }
        List<Address>addressList=addressRepository.findByUserId(userId);
        List<AddressResponse> responseList=new ArrayList<>();
        for(Address address: addressList){
            responseList.add(mapper.AddressToResponse(address));
        }
        return responseList;
    }

}
