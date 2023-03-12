package com.flashcard.flashback.usertests;

import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.user.data.UserDao;
import com.flashcard.flashback.user.data.UserDto;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.repository.UserRepository;
import com.flashcard.flashback.user.service.UserService;
import com.flashcard.flashback.verification.entity.VerificationToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UsersEntity testUser;

    @Before
    public void setup() {
        testUser = new UsersEntity("login", "username",
                "email@example.com", "password");
    }

    @Test
    public void mockNotNull() {
        assertNotNull(userService);
        assertNotNull(userRepository);
    }

    @Test
    public void getUserByLoginTest() {
        when(userRepository.findByLogin("login")).thenReturn(Optional.of(testUser));

        UsersEntity user = userRepository.findByLogin("login").get();

        assertEquals("login", user.getLogin());
        assertEquals("username", user.getUsername());
        assertEquals("email@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
    }

    @Test
    public void saveUserTest() {
        userService.save(testUser);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    public void mapDtoTest() {
        UserDto dto = UserDto.builder()
                .email("email@example.com")
                .login("login")
                .password("password")
                .username("username")
                .build();
        UsersEntity user = userService.mapDto(dto);

        assertEquals(dto.getPassword(), user.getPassword());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getLogin(), user.getLogin());
        assertEquals(dto.getUsername(), user.getUsername());
    }

    @Test
    public void mapDaoTest() {
        UserDao dao = userService.toDao(testUser);

        assertEquals(dao.getUsername(), testUser.getUsername());
    }

    @Test
    public void checkEmailTest() {
        assertTrue(userService.checkEmail("test@example.com"));
        assertFalse(userService.checkEmail("test@.com"));
        assertFalse(userService.checkEmail("test@gmail."));
        assertFalse(userService.checkEmail("test@gmail"));
        assertFalse(userService.checkEmail("test.com"));
    }

    @Test
    public void findByEmailOrLoginThrowEntityNotFoundExceptionTest() {
        assertThrows(EntityNotFoundException.class, () -> userService.findByEmailOrLogin("invalid"));
    }

    @Test
    public void confirmEmailTest() {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUsersEntity(testUser);
        userService.confirmEmail(verificationToken);

        assertTrue(testUser.isVerified());
    }
}
