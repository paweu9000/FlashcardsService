package com.flashcard.flashback.test;

import com.flashcard.flashback.card.CardEntity;
import com.flashcard.flashback.collection.CollectionEntity;
import com.flashcard.flashback.collection.CollectionService;
import com.flashcard.flashback.user.UsersEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestServiceTests {

    @Mock
    CollectionService collectionService;

    @Mock
    TestRepository testRepository;

    @InjectMocks
    TestService testService;
    UsersEntity user;
    CollectionEntity collection;

    @Before
    public void setup() {
        user = new UsersEntity();
        user.setId(1L);
        collection = new CollectionEntity(1L, "title", 1L, new HashSet<>(), user);
        for(int i = 1; i <= 10; i++) {
            collection.addCard(new CardEntity(Long.valueOf(i+""), "side" + i, "value" + i, collection, user));
        }
    }

    @Test
    public void notNullTest() {
        assertNotNull(collectionService);
        assertNotNull(testRepository);
        assertNotNull(testService);
        assertNotNull(testService.collectionService);
        assertNotNull(testService.repository);
        assertNotNull(user);
        assertNotNull(collection);
        assertEquals(10, collection.getSize());
    }

    @Test
    public void getTestEntityByCollectionIdExistTest() {
        TestEntity test = new TestEntity();
        when(testRepository.findByCollectionId(1L)).thenReturn(Optional.of(test));
        testService.getTestEntityByCollectionId(1L);
        verify(testRepository, times(1)).findByCollectionId(1L);
        verify(testRepository, times(0)).save(any(TestEntity.class));
    }
}
