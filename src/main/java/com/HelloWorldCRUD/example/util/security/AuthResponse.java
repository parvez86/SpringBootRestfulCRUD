package com.HelloWorldCRUD.example.util.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

//@Data
@Component
@NoArgsConstructor
public class AuthResponse {
    private String email;
    private String accessToken;

//    public AuthResponse(String email, String accessToken) {
//        this.email = email;
//        this.accessToken = accessToken;
//    }
    public void setAuthResponse(String email, String accessToken){
        this.email = email;
        this.accessToken = accessToken;
    }
}
