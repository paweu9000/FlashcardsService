package com.flashcard.flashback.verification.service;

import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.verification.entity.VerificationToken;
import com.flashcard.flashback.verification.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository repository;

    public VerificationTokenService(VerificationTokenRepository repository) {
        this.repository = repository;
    }

    public VerificationToken generateVerificationToken(UsersEntity user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = mapToken(token, user);
        return repository.save(verificationToken);
    }

    public VerificationToken getVerificationToken(String token) {
        return unwrapVerificationToken(repository.findByToken(token));
    }

    public VerificationToken unwrapVerificationToken(Optional<VerificationToken> token) {
        if (token.isPresent()) return token.get();
        else throw new EntityNotFoundException(VerificationToken.class);
    }

    public VerificationToken mapToken(String token, UsersEntity usersEntity) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUsersEntity(usersEntity);
        return verificationToken;
    }
}
