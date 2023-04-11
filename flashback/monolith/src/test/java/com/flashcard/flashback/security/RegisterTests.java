package com.flashcard.flashback.security;

import com.flashcard.flashback.email.EmailService;
import com.flashcard.flashback.email.VerificationToken;
import com.flashcard.flashback.email.VerificationTokenRepository;
import com.flashcard.flashback.email.VerificationTokenService;
import com.flashcard.flashback.user.UserMapper;
import com.flashcard.flashback.user.UserRepository;
import com.flashcard.flashback.user.UserService;
import com.flashcard.flashback.user.UsersEntity;
import com.flashcard.flashback.user.data.UserDto;
import org.hibernate.search.annotations.Indexed;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegisterTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private VerificationTokenRepository tokenRepository;
    @Spy
    @InjectMocks
    private VerificationTokenService tokenService;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private UserService userService;
    UsersEntity user;
    UserDto userDto;
    @Before
    public void setUp() {
        user = new UsersEntity("login", "username",
                "email@example.com", "password");
        userService.setTokenService(tokenService);
        userDto = UserMapper.INSTANCE.entityToDto(user);
    }

    @Test
    public void mockNotNull() {
        assertNotNull(userRepository);
        assertNotNull(passwordEncoder);
        assertNotNull(tokenRepository);
        assertNotNull(tokenService);
        assertNotNull(emailService);
        assertNotNull(userService);
        assertNotNull(user);
        assertNotNull(userDto);
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

        assertNotNull(verificationToken);
        verify(userRepository).save(any(UsersEntity.class));
        verify(tokenRepository).save(any(VerificationToken.class));
    }
}
