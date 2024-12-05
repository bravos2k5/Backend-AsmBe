package com.bravos2k5.asmbe.service;

import com.bravos2k5.asmbe.model.RefreshToken;
import com.bravos2k5.asmbe.model.User;
import com.bravos2k5.asmbe.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder().
                user(user).
                expire(LocalDateTime.now().plusDays(61)).
                active(true).
                build();
        refreshTokenRepository.saveAndFlush(refreshToken);
        return refreshToken;
    }

    public RefreshToken inActiveRefreshToken(UUID id) {
        RefreshToken refreshToken = refreshTokenRepository.findById(id).orElse(null);
        if(refreshToken == null) {
            throw new IllegalArgumentException("This refresh token hasn't been created");
        }
        refreshToken.setActive(false);
        refreshTokenRepository.saveAndFlush(refreshToken);
        return refreshToken;
    }

    public RefreshToken getRefreshTokenIfValid(UUID id) {
        RefreshToken refreshToken = refreshTokenRepository.findById(id).orElse(null);
        return refreshToken != null && refreshToken.getExpire().isAfter(LocalDateTime.now()) ? refreshToken : null;
    }

}
