package com.flashcard.flashback.verification.service;

import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.service.UserService;
import com.flashcard.flashback.verification.entity.VerificationToken;
import com.flashcard.flashback.verification.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository repository;
    private final UserService userService;

    public VerificationTokenService(VerificationTokenRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public VerificationToken generateVerificationToken(UsersEntity user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUsersEntity(user);
        user.setVerified(true);
        userService.save(user);
        return repository.save(verificationToken);
    }

    public VerificationToken getVerificationToken(String token) {
        return repository.findByToken(token).get();
    }
}
