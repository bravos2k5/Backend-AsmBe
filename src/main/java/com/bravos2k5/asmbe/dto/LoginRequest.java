package com.bravos2k5.asmbe.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.bravos2k5.asmbe.model.User}
 */

public record LoginRequest(String username, String password) implements Serializable {
}