package com.bravos2k5.asmbe.dto;

public record LoginResponse(String message, UserInfo userInfo, String accessToken, String refreshToken) {
}
