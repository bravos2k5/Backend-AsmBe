package com.bravos2k5.asmbe.dto;

import java.io.Serializable;


public record RegisterRequest(String username, String password, String name, String email) implements Serializable {
}