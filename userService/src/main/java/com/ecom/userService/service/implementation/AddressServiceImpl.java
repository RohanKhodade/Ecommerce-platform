package com.ecom.userService.service.implementation;

import com.ecom.userService.dto.Mapper;
import com.ecom.userService.dto.request.AddAddressRequest;
import com.ecom.userService.dto.request.UpdateAddressRequest;
import com.ecom.userService.dto.response.AddressResponse;
import com.ecom.userService.entity.Address;
import com.ecom.userService.entity.User;
import com.ecom.userService.repository.AddressRepository;
import com.ecom.userService.repository.UserRepository;
import com.ecom.userService.service.services.AddressService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()){
            User user=optionalUser.get();
            Address address=mapper.AddressReqToAddress(request);
            address.setUser(user);
            return mapper.AddressToResponse(addressRepository.save(address));
        }
        return null;
    }

    @Override
    public AddressResponse updateAddress(Long userId,
                                         Long addressId,
                                         UpdateAddressRequest request) {
        Optional<Address> address=addressRepository.findByIdAndUserId(addressId,userId);
        if (address.isPresent()){
            Address address1=address.get();
            address1.setBuilding(request.getBuilding());
            address1.setStreet(request.getStreet());
            address1.setCity(request.getCity());
            address1.setZip(request.getZip());
            address1.setState(request.getState());
            address1.setCountry(request.getCountry());
            address1.setUser(address1.getUser());
            addressRepository.save(address1);
            return mapper.AddressToResponse(address1);
        }
        return null;
    }

    @Override
    public String deleteAddress(Long userId, Long addressId) {
        Optional<Address> address=addressRepository.findByIdAndUserId(addressId, userId);
        if (address.isPresent()){
            addressRepository.deleteById(addressId);
            return "Address Deleted";
        }
        else{
            return "Address Not Found OR you must delete your address only";
        }

    }

    @Override
    public AddressResponse getAddress(Long userId, Long addressId) {
        Optional<Address> address=addressRepository.findByIdAndUserId(addressId,userId);
        if (address.isPresent()){
            return mapper.AddressToResponse(address.get());
        }
        return null;
    }

    @Override
    public List<AddressResponse> getAllAddresses(Long userId) {
        List<Address>addressList=addressRepository.findByUserId(userId);
        List<AddressResponse> responseList=new ArrayList<>();
        for(Address address: addressList){
            responseList.add(mapper.AddressToResponse(address));
        }
        return responseList;

    }

}
