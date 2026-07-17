package com.ecom.orderService.clients.userService;


import com.ecom.orderService.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="userService", configuration= FeignClientConfig.class)
public interface UserClient {

    @GetMapping("/api/user/address/getAll/{userId}")
    List<UserAddressResponse> getUserAddress(@PathVariable Long userId);
}
