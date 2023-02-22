package com.flashcard.flashback.collectiontests;

import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.data.CollectionDto;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.collection.repository.CollectionRepository;
import com.flashcard.flashback.collection.service.CollectionService;
import com.flashcard.flashback.exception.UnauthorizedDataCreateException;
import com.flashcard.flashback.exception.UnauthorizedDataDeleteException;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.repository.UserRepository;
import com.flashcard.flashback.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CollectionServiceTests {

    @Mock
    private CollectionRepository collectionRepository;
    @Mock
    private Authentication authentication;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    @Spy
    private UserService userService;
    @InjectMocks
    private CollectionService collectionService;

    @Before
    public void injectUserService() {
        collectionService.setUserService(userService);
    }

    @Test
    public void toDaoTest() {
        CollectionEntity collection = new CollectionEntity();
        collection.setLikes(5L);
        UsersEntity user = new UsersEntity();
        user.setId(23L);
        collection.setOwners(user);
        CollectionDao collectionDao = collectionService.toDao(collection);

        assertEquals(collection.getLikes(), collectionDao.getLikes());
        assertEquals(collection.getOwners().getId(), user.getId());
    }

    @Test
    public void findByIdTest() {
        CollectionEntity collection = new CollectionEntity();
        collection.setId(1L);
        collection.setLikes(2L);
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        assertEquals(1L, collectionRepository.findById(1L).get().getId());
        assertEquals(2L, collectionRepository.findById(1L).get().getLikes());
    }

    @Test
    public void existsTest() {
        CollectionEntity collection = new CollectionEntity(1L, 2L, null, null);

        assertEquals(collection.getId(), collectionService.exists(collection).getId());
        assertEquals(collection.getLikes(), collectionService.exists(collection).getLikes());
        assertThrows(RuntimeException.class, () -> collectionService.exists(null));
    }

    @Test
    public void deleteCollectionByIdTest() {
        Long id = 3L;
        collectionService.deleteCollectionById(id);

        verify(collectionRepository).deleteById(id);
    }

//    @Test
//    public void upvoteCollectionTest() {
//        CollectionEntity collection = new CollectionEntity(2L, 0L, null, null);
//        when(collectionRepository.findById(2L)).thenReturn(Optional.of(collection));
//        collectionService.upvoteCollection(2L);
//        collectionService.upvoteCollection(2L);
//
//        assertEquals(2, collectionService.findById(2L).getLikes());
//    }

    @Test
    public void deleteIfAllowedValidTest() {
        when(authentication.getName()).thenReturn("email@example.com");
        UsersEntity usersEntity = new UsersEntity("login", null, "email@example.com", null);
        CollectionEntity collection = new CollectionEntity(1L, 30L, null, usersEntity);
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        assertDoesNotThrow(() -> collectionService.deleteIfAllowed(authentication, 1L));
    }

    @Test
    public void deleteIfAllowedInvalidTest() {
        when(authentication.getName()).thenReturn("invalid@email.com");
        UsersEntity usersEntity = new UsersEntity("login", null, "email@example.com", null);
        CollectionEntity collection = new CollectionEntity(1L, 30L, null, usersEntity);
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        assertThrows(UnauthorizedDataDeleteException.class, () -> collectionService
                .deleteIfAllowed(authentication, 1L));
    }

    @Test
    public void createIfAllowedValidTest() {
        when(authentication.getName()).thenReturn("email@example.com");
        UsersEntity usersEntity = new UsersEntity("login", null, "email@example.com", null);
        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setLikes(21L);
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.of(usersEntity));
        collectionService.createCollection(authentication.getName(), collectionDto);

        assertEquals(userService.findByEmailOrLogin("email@example.com").getCollections().get(0).getLikes(), 21L);
    }

    @Test
    @WithAnonymousUser
    public void createIfAllowedInvalidTest() {
        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setLikes(21L);

        assertThrows(UnauthorizedDataCreateException.class,
                () -> collectionService.createCollection(null, collectionDto));
    }
}
