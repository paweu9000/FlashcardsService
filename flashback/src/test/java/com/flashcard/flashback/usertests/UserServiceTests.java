package com.flashcard.flashback.usertests;

import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.repository.UserRepository;
import com.flashcard.flashback.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void getUserByLoginTest() {
        UsersEntity toReturn = new UsersEntity("login", "username",
                "email@example.com", "password");
        when(userRepository.findByLogin("login")).thenReturn(Optional.of(toReturn));

        UsersEntity user = userRepository.findByLogin("login").get();

        assertEquals("login", user.getLogin());
        assertEquals("username", user.getUsername());
        assertEquals("email@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
    }
}
