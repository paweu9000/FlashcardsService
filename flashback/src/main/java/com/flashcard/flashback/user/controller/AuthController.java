package com.flashcard.flashback.user.controller;

import com.flashcard.flashback.user.data.UserDto;
import com.flashcard.flashback.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
public record AuthController (UserService userService) {

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerUser(@Valid @RequestBody UserDto userDto) {
        userService.register(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
