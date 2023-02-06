package com.flashcard.flashback.collectiontests;

import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.collection.repository.CollectionRepository;
import com.flashcard.flashback.collection.service.CollectionService;
import com.flashcard.flashback.user.entity.UsersEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CollectionServiceTests {

    @Mock
    private CollectionRepository collectionRepository;

    @InjectMocks
    private CollectionService collectionService;

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
}
