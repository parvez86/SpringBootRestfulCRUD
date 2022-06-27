package com.HelloWorldCRUD.example.util.security;

import com.HelloWorldCRUD.example.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    public boolean validateAccessToken(String token){
        try{
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e){
            LOGGER.error("JWT expired", e.getMessage());
        }catch (IllegalArgumentException e){
            LOGGER.error("JWT Token is null, empty or whitespace", e.getMessage());
        }
        catch (MalformedJwtException e){
            LOGGER.error("JWT is invalid", e.getMessage());
        }catch (UnsupportedJwtException e){
            LOGGER.error("JWT is not suported", e.getMessage());
        }catch (SignatureException e){
            LOGGER.error("Signature validation failed", e.getMessage());
        }
        return false;
    }

    public String getSubject(String token){
        return paresClaims(token).getSubject();
    }

    private Claims paresClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }


    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
                .setIssuer("test")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
//                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

    }
}