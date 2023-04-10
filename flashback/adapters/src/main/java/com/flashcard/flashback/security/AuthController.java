package com.flashcard.flashback.security;

import com.flashcard.flashback.email.EmailService;
import com.flashcard.flashback.user.UserService;
import com.flashcard.flashback.user.data.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/auth")
record AuthController (UserService userService, EmailService emailService) {

    @PostMapping("/register")
    ResponseEntity<HttpStatus> registerUser(@Valid @RequestBody UserDto userDto) throws MessagingException {
        userService.register(userDto);
        emailService.sendVerificationEmail(userDto.getEmail(), UUID.randomUUID().toString());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
