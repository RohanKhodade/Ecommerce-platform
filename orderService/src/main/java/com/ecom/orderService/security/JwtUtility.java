package com.ecom.orderService.security;

import com.ecom.orderService.exceptions.JwtTokenNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtility {

    private final HttpServletRequest request;
    public JwtUtility(HttpServletRequest request) {
        this.request = request;
    }
    private SecretKey getKey(){
        String SECRET="secret key for creating jwt token";
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
    public Long extractUserIdFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("userId",Long.class);
    }
    public String extractToken(){
        String header=request.getHeader("Authorization");
        if(header!= null && !header.startsWith("Bearer ")){
            return header.substring(7);
        }
        throw new JwtTokenNotFoundException("no jwt token found");
    }
}
