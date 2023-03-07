package com.flashcard.flashback.verification.service;

import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.verification.entity.VerificationToken;
import com.flashcard.flashback.verification.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository repository;

    public VerificationTokenService(VerificationTokenRepository repository) {
        this.repository = repository;
    }

    public VerificationToken generateVerificationToken(UsersEntity user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUsersEntity(user);
        return repository.save(verificationToken);
    }

    public VerificationToken getVerificationToken(String token) {
        return repository.findByToken(token).get();
    }
}
