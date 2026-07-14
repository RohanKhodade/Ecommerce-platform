package com.ecom.orderService.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAccessGuard {

    private final JwtUtility jwtUtility;
    public UserAccessGuard(JwtUtility jwtUtility) {
        this.jwtUtility = jwtUtility;
    }

    public boolean isUserVerified(Long userId){
        String token=jwtUtility.extractToken();
        Long jwtUserId=jwtUtility.extractUserIdFromToken(token);
        return userId.equals(jwtUserId);
    }

}
