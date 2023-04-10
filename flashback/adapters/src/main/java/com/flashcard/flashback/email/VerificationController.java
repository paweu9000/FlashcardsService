package com.flashcard.flashback.email;

import com.flashcard.flashback.user.UserService;
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
        userService.confirmEmail(tokenService.getVerificationToken(token));
        return new ResponseEntity<>("Email verified successfully", HttpStatus.OK);
    }
}
