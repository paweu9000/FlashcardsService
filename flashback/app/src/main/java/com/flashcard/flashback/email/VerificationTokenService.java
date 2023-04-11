package com.flashcard.flashback.email;

import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.user.UsersEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {
    private final VerificationTokenRepository repository;

    VerificationTokenService(VerificationTokenRepository repository) {
        this.repository = repository;
    }

    public VerificationToken generateVerificationToken(UsersEntity user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = mapToken(token, user);
        return repository.save(verificationToken);
    }

    VerificationToken getVerificationToken(String token) {
        return unwrapVerificationToken(repository.findByToken(token));
    }

    VerificationToken unwrapVerificationToken(Optional<VerificationToken> token) {
        if (token.isPresent()) return token.get();
        else throw new EntityNotFoundException(VerificationToken.class);
    }

    VerificationToken mapToken(String token, UsersEntity usersEntity) {
        return TokenMapper.INSTANCE.mapToken(token, usersEntity);
    }
}
