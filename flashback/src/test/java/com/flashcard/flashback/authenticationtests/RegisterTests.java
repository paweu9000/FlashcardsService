package com.flashcard.flashback.authenticationtests;

import com.flashcard.flashback.user.data.UserDto;
import com.flashcard.flashback.user.data.mapper.UserMapper;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.repository.UserRepository;
import com.flashcard.flashback.user.service.UserService;
import com.flashcard.flashback.verification.entity.VerificationToken;
import com.flashcard.flashback.verification.mapper.TokenMapper;
import com.flashcard.flashback.verification.repository.VerificationTokenRepository;
import com.flashcard.flashback.verification.service.EmailService;
import com.flashcard.flashback.verification.service.VerificationTokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegisterTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private VerificationTokenRepository repository;

    @Mock
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
    }

    @Test
    public void mockNotNull() {
        assertNotNull(userRepository);
        assertNotNull(passwordEncoder);
        assertNotNull(repository);
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
}
