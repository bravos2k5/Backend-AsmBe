package com.bravos2k5.asmbe.controller;

import com.bravos2k5.asmbe.dto.UserInfo;
import com.bravos2k5.asmbe.model.User;
import com.bravos2k5.asmbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public ResponseEntity<UserInfo> getUserInfo() {
        String authUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(authUsername);
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new UserInfo(user.getUsername(),user.getName(),user.getAvatar(),user.getEmail()),HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<UserInfo> updateUser(@RequestBody UserInfo userUpdate) {
        String authUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!authUsername.equals(userUpdate.username())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        UserInfo updatedInfo = userService.update(userUpdate);
        return new ResponseEntity<>(updatedInfo, HttpStatus.OK);
    }

}
