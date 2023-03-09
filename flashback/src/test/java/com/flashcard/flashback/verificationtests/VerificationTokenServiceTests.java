package com.flashcard.flashback.verificationtests;

import com.flashcard.flashback.verification.repository.VerificationTokenRepository;
import com.flashcard.flashback.verification.service.VerificationTokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class VerificationTokenServiceTests {

    @Mock
    VerificationTokenRepository repository;

    @InjectMocks
    VerificationTokenService service;

    @Test
    public void notNull() {
        assertNotNull(repository);
        assertNotNull(service);
    }
}
