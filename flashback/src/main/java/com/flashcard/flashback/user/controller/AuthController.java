package com.flashcard.flashback.user.controller;

import com.flashcard.flashback.user.UsersEntity;
import com.flashcard.flashback.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public record AuthController (UserService userService) {

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Validated UsersEntity user) {
        userService.save(user);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

}
