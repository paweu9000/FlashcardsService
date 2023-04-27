package com.flashcard.flashback.collection;

import com.flashcard.flashback.card.CardEntity;
import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.data.CollectionDto;
import com.flashcard.flashback.exception.SavedCollectionDuplicateException;
import com.flashcard.flashback.exception.UnauthorizedDataCreateException;
import com.flashcard.flashback.exception.UnauthorizedDataDeleteException;
import com.flashcard.flashback.user.UserRepository;
import com.flashcard.flashback.user.UserService;
import com.flashcard.flashback.user.UsersEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    @Mock
    ApplicationEventPublisher eventPublisher;
    @InjectMocks
    private CollectionService collectionService;

    private UsersEntity userTest;

    @Before
    public void setup() {
        collectionService.setUserService(userService);
        userTest = new UsersEntity();
        userTest.setId(1L);
        userTest.setUsername("username");
        UsersEntity userTest2 = new UsersEntity();
        userTest2.setId(2L);
        userTest2.setUsername("username2");
        CollectionEntity collection = new CollectionEntity(1L, "title1",
                1L, new HashSet<>(), userTest);
        collection.addCard(new CardEntity(1L, "side", "value", collection, userTest));
        CollectionEntity collection1 = new CollectionEntity(2L, "title2",
                2L, new HashSet<>(), userTest2);
        userTest.addCollection(collection);
        userTest.saveCollection(collection1);
    }

    @Test
    public void mockNotNull() {
        assertNotNull(userService);
        assertNotNull(collectionService);
        assertNotNull(userRepository);
        assertNotNull(authentication);
        assertNotNull(collectionRepository);
    }

    @Test
    public void toDaoTest() {
        CollectionEntity collection = new CollectionEntity();
        collection.setLikes(5L);
        UsersEntity user = new UsersEntity();
        user.setId(23L);
        collection.setOwners(user);
        CollectionDao collectionDao = collectionService.toDao(collection);

        assertNotNull(collection);
        assertNotNull(collectionDao);
        assertEquals(collection.getLikes(), collectionDao.getLikes());
        assertEquals(collection.getOwners().getId(), user.getId());
    }

    @Test
    public void toSortedDaoTest() {
        CollectionEntity collection = new CollectionEntity();
        collection.setLikes(5L);
        UsersEntity user = new UsersEntity();
        user.setId(23L);
        collection.setOwners(user);
        for (int i = 1; i <= 10; i++) {
            collection.addCard(new CardEntity(Long.valueOf(String.valueOf(i)), "side"+i, "value"+i, collection, user));
        }
        CollectionDao collectionDao = collectionService.toSortedDao(collection);

        assertNotNull(collection);
        assertNotNull(collectionDao);
        assertEquals(collection.getLikes(), collectionDao.getLikes());
        assertEquals(collection.getOwners().getId(), user.getId());
        assertEquals(10, collectionDao.getCards().size());
        for (int i = 1; i <= 10; i++) {
            assertEquals(i, collectionDao.getCards().get(i-1).getId());
        }
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
        CollectionEntity collection = new CollectionEntity(1L, "title", 2L, null, null);

        assertEquals(collection.getId(), collectionService.exists(Optional.of(collection)).getId());
        assertEquals(collection.getLikes(), collectionService.exists(Optional.of(collection)).getLikes());
        assertThrows(RuntimeException.class, () -> collectionService.exists(null));
    }

    @Test
    public void deleteCollectionByIdTest() {
        Long id = 3L;
        collectionService.deleteCollectionById(id);

        verify(collectionRepository).deleteById(id);
    }

    @Test
    public void upvoteCollectionTest() {
        UsersEntity usersEntity = new UsersEntity("login", null, "email@example.com", null);
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.of(usersEntity));
        CollectionEntity collection = new CollectionEntity(2L, "title", 0L, null, null);
        when(collectionRepository.findById(2L)).thenReturn(Optional.of(collection));
        collectionService.upvoteCollection(2L, "email@example.com");

        assertNotNull(usersEntity);
        assertNotNull(collection);
        verify(userRepository).findByEmail("email@example.com");
        verify(collectionRepository).findById(2L);
        assertEquals(1, collectionService.findById(2L).getLikes());
    }

    @Test
    public void invalidUpvoteCollectionTest() {
        UsersEntity usersEntity = new UsersEntity("login", null, "email@example.com", null);
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.of(usersEntity));
        CollectionEntity collection = new CollectionEntity(2L, "title", 0L, null, null);
        when(collectionRepository.findById(2L)).thenReturn(Optional.of(collection));
        collectionService.upvoteCollection(2L, "email@example.com");

        assertThrows(SavedCollectionDuplicateException.class, () ->
                collectionService.upvoteCollection(2L, "email@example.com"));
        assertEquals(1, collection.getLikes());
    }

    @Test
    public void deleteIfAllowedValidTest() {
        UsersEntity usersEntity = new UsersEntity("login", null, "email@example.com", null);
        CollectionEntity collection = new CollectionEntity(1L, "title", 30L, null, usersEntity);
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        assertDoesNotThrow(() -> collectionService.deleteIfAllowed("email@example.com", 1L));
    }

    @Test
    public void deleteIfAllowedInvalidTest() {
        UsersEntity usersEntity = new UsersEntity("login", null, "email@example.com", null);
        CollectionEntity collection = new CollectionEntity(1L, "title", 30L, null, usersEntity);
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        assertThrows(UnauthorizedDataDeleteException.class, () -> collectionService
                .deleteIfAllowed("invalid@email.com", 1L));
    }

    @Test
    public void createIfAllowedValidTest() {
        when(authentication.getName()).thenReturn("email@example.com");
        UsersEntity usersEntity = new UsersEntity("login", null, "email@example.com", null);
        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setLikes(21L);
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.of(usersEntity));
        collectionService.createCollection(authentication.getName(), collectionDto);

        assertEquals(userService.findByEmailOrLogin("email@example.com")
                .getCollections().stream().findFirst().get().getLikes(), 21L);
    }

    @Test
    public void createIfAllowedInvalidTest() {
        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setLikes(21L);

        assertThrows(UnauthorizedDataCreateException.class,
                () -> collectionService.createCollection(null, collectionDto));
    }

    @Test
    public void findCollectionsEmptyTest() {
        assertThrows(IllegalArgumentException.class, () -> collectionService.searchByTitle("title"));
    }

    @Test
    public void mapDtoTest() {
        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setTitle("Title");
        collectionDto.setLikes(200L);
        CollectionEntity entity = collectionService.mapDto(collectionDto);

        assertEquals(entity.getLikes(), collectionDto.getLikes());
        assertEquals(entity.getTitle(), collectionDto.getTitle());
    }

    @Test
    public void toSortedCollectionDaoTest() {
        List<CollectionDao> list = new ArrayList<>();

        for(int i = 10; i >= 1; i--) {
            CollectionDao collectionDao = new CollectionDao();
            collectionDao.setId(Long.parseLong(String.valueOf(i)));
            list.add(collectionDao);
        }

        collectionService.toSortedCollectionListDao(list);

        assertNotNull(list);
        assertEquals(10, list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(Long.parseLong(String.valueOf(i+1)), list.get(i).getId());
        }
    }

    @Test
    public void findPersonalCollectionsTest() {
        List<CollectionDao> collectionDaos = collectionService.findPersonalCollections(userTest);

        assertNotNull(collectionDaos);
        assertEquals(1, collectionDaos.size());
        assertEquals(1L, collectionDaos.get(0).getId());
        assertEquals("title1", collectionDaos.get(0).getTitle());
    }
}
