package com.flashcard.flashback.authenticationtests;

import com.flashcard.flashback.user.data.UserDto;
import com.flashcard.flashback.user.data.mapper.UserMapper;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.repository.UserRepository;
import com.flashcard.flashback.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegisterTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;
    UsersEntity user;
    @Before
    public void setUp() {
        user = new UsersEntity("login", "username",
                "email@example.com", "password");
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
    public void registerValidTest() {
        UserDto userDto = UserMapper.INSTANCE.entityToDto(user);
        userService.register(userDto);
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).findByUsername("username");
        verify(userRepository, times(1)).findByEmail("email@example.com");
        verify(userRepository, times(1)).findByLogin("login");
    }

}
