package com.ecom.userService.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


// only knows how to create and read tokens
//doesnt know about http request, security context , pure token logic only
@Component
public class JwtUtility {

    // turns our plain string into a proper crypto key object for signing/verifying
    private SecretKey getKey(){
        String secret = "secret key for creating jwt token";
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    private final long EXPIRATION_TIME=60*60*1000;

    //called once at login after password and email is verified
    //builds the token and hand back to client
    public String generateToken(String userEmail){
        return Jwts.builder()
                .subject(userEmail) // whose token is this
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(getKey())
                .compact();
    }

    // called on every request inside jwtAuth filter to read token back
    public String validateToken(String token){
        Claims body=Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)// this single verifies the signature and checks expiration
                // throws if fails
                .getPayload();
        return body.getSubject();
    }
}
