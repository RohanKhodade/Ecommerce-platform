package com.ecom.orderService.clients.userService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="userService")
public interface UserClient {

    @GetMapping("/api/user")
    String getUserAddress(@PathVariable Long userId);
}
