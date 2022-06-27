package com.HelloWorldCRUD.example.security;

import com.HelloWorldCRUD.example.entity.User;
import com.HelloWorldCRUD.example.util.response.ApiResponse;
import com.HelloWorldCRUD.example.util.security.AuthRequest;
import com.HelloWorldCRUD.example.util.security.AuthResponse;
import com.HelloWorldCRUD.example.util.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.HelloWorldCRUD.example.entity.HttpStatus.SUCCESS;

@RestController
public class AuthApi {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                    )
            );

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtTokenUtil.generateAccessToken(user);
            AuthResponse authResponse = new AuthResponse();
            authResponse.setAuthResponse(user.getEmail(), accessToken);
            return ResponseEntity.status(HttpStatus.OK).body(authResponse);
        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
