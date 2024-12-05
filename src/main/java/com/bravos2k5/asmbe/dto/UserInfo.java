package com.bravos2k5.asmbe.dto;

import com.bravos2k5.asmbe.model.User;

import java.io.Serializable;


public record UserInfo(String username, String name, String avatar, String email) implements Serializable {

}