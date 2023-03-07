package com.flashcard.flashback.verification.controller;

import com.flashcard.flashback.user.service.UserService;
import com.flashcard.flashback.verification.entity.VerificationToken;
import com.flashcard.flashback.verification.service.VerificationTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/verify")
public record VerificationController(VerificationTokenService tokenService, UserService userService) {

    @PostMapping
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        VerificationToken verificationToken = tokenService.getVerificationToken(token);
        tokenService.generateVerificationToken(verificationToken.getUsersEntity());
        return new ResponseEntity<>("Email verified successfully", HttpStatus.OK);
    }
}
