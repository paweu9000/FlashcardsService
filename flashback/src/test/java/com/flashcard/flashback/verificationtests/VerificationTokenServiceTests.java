package com.flashcard.flashback.verificationtests;

import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.verification.entity.VerificationToken;
import com.flashcard.flashback.verification.repository.VerificationTokenRepository;
import com.flashcard.flashback.verification.service.VerificationTokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerificationTokenServiceTests {

    @Mock
    VerificationTokenRepository repository;

    @InjectMocks
    VerificationTokenService service;
    VerificationToken verificationToken;
    UsersEntity usersEntity;

    @Before
    public void setup() {
        verificationToken = new VerificationToken();
        verificationToken.setToken("mapping-test-uuid-1234");
        usersEntity = new UsersEntity("login", "username", "email@example.com", "password");
        verificationToken.setUsersEntity(usersEntity);
    }

    @Test
    public void notNull() {
        assertNotNull(repository);
        assertNotNull(service);
        assertNotNull(verificationToken);
        assertNotNull(usersEntity);
    }

    @Test
    public void generateTokenTest() {
        when(repository.save(any(VerificationToken.class))).thenReturn(verificationToken);
        VerificationToken token = service.generateVerificationToken(usersEntity);
        verify((repository)).save(any(VerificationToken.class));
        assertNotNull(token);
        assertEquals(token.getUsersEntity(), usersEntity);
        assertEquals(token.getToken(), verificationToken.getToken());
    }

    @Test
    public void mapTokenTest() {
        VerificationToken token = service.mapToken(verificationToken.getToken(), usersEntity);

        assertNotNull(token);
        assertEquals(token.getToken(), verificationToken.getToken());
        assertEquals(token.getUsersEntity(), verificationToken.getUsersEntity());
    }

    @Test
    public void unwrapVerificationTokenValidTest() {
        VerificationToken token = service.unwrapVerificationToken(Optional.of(verificationToken));

        assertNotNull(token);
        assertEquals(token.getToken(), verificationToken.getToken());
        assertEquals(token.getUsersEntity(), verificationToken.getUsersEntity());
    }

    @Test
    public void unwrapVerificationTokenInvalidTest() {
        assertThrows(EntityNotFoundException.class, () -> service.unwrapVerificationToken(Optional.empty()));
    }
}
