package com.flashcard.flashback.authenticationtests;

import com.flashcard.flashback.user.data.UserDto;
import com.flashcard.flashback.user.data.mapper.UserMapper;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.repository.UserRepository;
import com.flashcard.flashback.user.service.UserService;
import com.flashcard.flashback.verification.entity.VerificationToken;
import com.flashcard.flashback.verification.repository.VerificationTokenRepository;
import com.flashcard.flashback.verification.service.EmailService;
import com.flashcard.flashback.verification.service.VerificationTokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegisterTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private VerificationTokenRepository tokenRepository;
    private VerificationTokenService tokenService;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private UserService userService;
    UsersEntity user;
    @Before
    public void setUp() {
        user = new UsersEntity("login", "username",
                "email@example.com", "password");
        MockitoAnnotations.openMocks(this);
        tokenService = new VerificationTokenService(tokenRepository);
        userService.setTokenService(tokenService);
    }

    @Test
    public void mockNotNull() {
        assertNotNull(userRepository);
        assertNotNull(passwordEncoder);
        assertNotNull(tokenRepository);
        assertNotNull(tokenService);
        assertNotNull(emailService);
        assertNotNull(userService);
    }

    @Test
    public void registerLoginExistsTest() {
        UserDto userDto = UserMapper.INSTANCE.entityToDto(user);
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        assertThrows(ResponseStatusException.class, () -> userService.register(userDto));
    }
    @Test
    public void registerEmailExistsTest() {
        UserDto userDto = UserMapper.INSTANCE.entityToDto(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(ResponseStatusException.class, () -> userService.register(userDto));
    }
    @Test
    public void registerUsernameExistsTest() {
        UserDto userDto = UserMapper.INSTANCE.entityToDto(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(ResponseStatusException.class, () -> userService.register(userDto));
    }

    @Test
    public void registerTest() throws MessagingException {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken("token");
        verificationToken.setUsersEntity(user);
        when(tokenRepository.save(any(VerificationToken.class))).thenReturn(verificationToken);
        UserDto userDto = UserMapper.INSTANCE.entityToDto(user);
        userService.register(userDto);

        verify(userRepository).save(any(UsersEntity.class));
        verify(tokenRepository).save(any(VerificationToken.class));
    }
}
