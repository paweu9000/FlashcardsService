package com.flashcard.flashback.authenticationtests;

import com.flashcard.flashback.user.data.UserDto;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.repository.UserRepository;
import com.flashcard.flashback.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegisterTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private String email = "email@example.com";
    private String password = "password";
    private String username = "username";
    private String login = "login";

    @Test
    public void registerLoginExistsTest() {

        UsersEntity usersEntity = new UsersEntity(login, username, email, password);
        UserDto userDto = UserDto.builder()
                .email(email)
                .login(login)
                .password(password)
                .username(username).build();
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(usersEntity));

        assertThrows(ResponseStatusException.class, () -> userService.register(userDto));
    }
    @Test
    public void registerEmailExistsTest() {

        UsersEntity usersEntity = new UsersEntity(login, username, email, password);
        UserDto userDto = UserDto.builder()
                .email(email)
                .login(login)
                .password(password)
                .username(username).build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(usersEntity));

        assertThrows(ResponseStatusException.class, () -> userService.register(userDto));
    }
    @Test
    public void registerUsernameExistsTest() {

        UsersEntity usersEntity = new UsersEntity(login, username, email, password);
        UserDto userDto = UserDto.builder()
                .email(email)
                .login(login)
                .password(password)
                .username(username).build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(usersEntity));

        assertThrows(ResponseStatusException.class, () -> userService.register(userDto));
    }

}
