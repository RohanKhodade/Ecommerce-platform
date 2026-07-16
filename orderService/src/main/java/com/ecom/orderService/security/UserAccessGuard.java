package com.ecom.orderService.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAccessGuard {

    private final JwtUtility jwtUtility;
    private final AuthUtil authUtil;
    public UserAccessGuard(JwtUtility jwtUtility,
                           AuthUtil authUtil) {
        this.jwtUtility = jwtUtility;
        this.authUtil = authUtil;
    }


    public boolean isUserVerified(Long userId){
        String token=jwtUtility.extractToken();
        Long jwtUserId=jwtUtility.extractUserIdFromToken(token);
        return userId.equals(jwtUserId);
    }

}
