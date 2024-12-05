package com.bravos2k5.asmbe.service;

import com.bravos2k5.asmbe.dto.LoginRequest;
import com.bravos2k5.asmbe.dto.LoginResponse;
import com.bravos2k5.asmbe.dto.RegisterRequest;
import com.bravos2k5.asmbe.dto.UserInfo;
import com.bravos2k5.asmbe.model.User;
import com.bravos2k5.asmbe.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public UserService(UserRepository userRepository, JwtService jwtService, BCryptPasswordEncoder encoder, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.refreshTokenService = refreshTokenService;
    }

    public User findByUsername(String username) {
        return userRepository.findById(username).orElse(null);
    }

    public UserInfo getUserInfoFromToken(String token) {
        String username = jwtService.extractClaim(token, Claims::getSubject);
        if (jwtService.validateToken(token, username)) {
            User user = findByUsername(username);
            if (user != null) {
                return new UserInfo(user.getUsername(), user.getName(), user.getAvatar(), user.getEmail());
            }
        }
        return null;
    }

    public LoginResponse login(LoginRequest loginRequest) {

        if(loginRequest.username().isBlank() || loginRequest.password().isBlank()) {
            return new LoginResponse("Username and password mustn't be blank",null,"","");
        }

        Optional<User> optionalUser = userRepository.findById(loginRequest.username());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (encoder.matches(loginRequest.password(), user.getPassword())) {
                UserInfo userInfo = new UserInfo(user.getUsername(), user.getName(), user.getAvatar(), user.getEmail());
                Map<String, Object> claims = new HashMap<>();
                claims.put("sub", userInfo.username());
                String token = jwtService.generateToken(claims,60 * 60);
                String refreshToken = refreshTokenService.createRefreshToken(user).getId().toString();
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userInfo.username(),null));
                return new LoginResponse("Login successful",userInfo, token, refreshToken);
            }
        }
        return new LoginResponse("Login failed",null,"","");

    }

    public UserInfo register(RegisterRequest registerRequest) throws IllegalArgumentException {

        if (registerRequest.username().isBlank() ||
                registerRequest.password().isBlank() ||
                registerRequest.email().isBlank() ||
                registerRequest.name().isBlank()) {
            throw new IllegalArgumentException("Username, password, email and name mustn't be blank");
        }

        if (userRepository.existsByUsernameLikeIgnoreCase(registerRequest.username())) {
            throw new IllegalArgumentException("Username is existed");
        }

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setName(registerRequest.name());
        user.setEmail(registerRequest.email());
        user.setPassword(encoder.encode(registerRequest.password()));
        User u = userRepository.saveAndFlush(user);
        return new UserInfo(u.getUsername(), u.getName(), u.getAvatar(), u.getEmail());

    }

    public UserInfo update(UserInfo userInfo) {

        User user = findByUsername(userInfo.username());
        if(user == null) {
            throw new IllegalArgumentException("User is not exist");
        }
        user.setName(userInfo.name());
        user.setEmail(userInfo.email());
        user.setAvatar(userInfo.avatar());
        User updatedUser = userRepository.saveAndFlush(user);
        return new UserInfo(updatedUser.getUsername(),updatedUser.getName(),updatedUser.getAvatar(),updatedUser.getEmail());

    }

}
