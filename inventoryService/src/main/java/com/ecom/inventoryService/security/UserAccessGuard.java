package com.ecom.inventoryService.security;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class UserAccessGuard {

    private final JwtUtility jwtUtility;
    public UserAccessGuard(JwtUtility jwtUtility) {
        this.jwtUtility = jwtUtility;
    }
    public boolean isUserVerified(Long userId){
        String token= jwtUtility.extractToken();
        return jwtUtility.extractUserIdFromToken(token).equals(userId);
    }
}
