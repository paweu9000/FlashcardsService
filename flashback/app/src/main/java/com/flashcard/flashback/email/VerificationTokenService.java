package com.flashcard.flashback.email;

import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.user.UserService;
import com.flashcard.flashback.user.UsersEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {

    private final UserService userService;
    private VerificationTokenRepository repository;

    public VerificationTokenService(VerificationTokenRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public void setRepository(VerificationTokenRepository repository) {
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
        return TokenMapper.INSTANCE.mapToken(token, usersEntity);
    }
}