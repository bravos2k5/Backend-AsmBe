package com.bravos2k5.asmbe.controller;

import com.bravos2k5.asmbe.dto.*;
import com.bravos2k5.asmbe.service.JwtService;
import com.bravos2k5.asmbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<UserInfo> register(@RequestBody RegisterRequest registerRequest) {
        try {
            return new ResponseEntity<>(userService.register(registerRequest),HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> renewAccessToken(@RequestBody RefreshTokenRequest refreshToken) {
        String token = jwtService.generateToken(UUID.fromString(refreshToken.refreshToken()),60 * 60);
        return new ResponseEntity<>(token, token != null ? HttpStatus.OK : HttpStatus.REQUEST_TIMEOUT);
    }

}
