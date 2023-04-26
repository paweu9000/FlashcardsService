package com.flashcard.flashback.test;

import com.flashcard.flashback.card.CardEntity;
import com.flashcard.flashback.collection.CollectionEntity;
import com.flashcard.flashback.collection.CollectionObserver;
import com.flashcard.flashback.collection.CollectionRepository;
import com.flashcard.flashback.collection.CollectionService;
import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.exception.InsufficientQuestionsException;
import com.flashcard.flashback.test.data.TestDao;
import com.flashcard.flashback.user.UsersEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestServiceTests {

    @Mock
    CollectionRepository collectionRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Spy
    @InjectMocks
    CollectionService collectionService;

    @Mock
    TestRepository testRepository;

    @InjectMocks
    TestService testService;
    UsersEntity user;
    CollectionEntity collection;

    @Before
    public void setup() {
        testService.setCollectionService(collectionService);
        user = new UsersEntity();
        user.setId(1L);
        collection = new CollectionEntity(1L, "title", 1L, new HashSet<>(), user);
        for(int i = 1; i <= 10; i++) {
            collection.addCard(new CardEntity(Long.valueOf(String.valueOf(i)), "side" + i, "value" + i, collection, user));
        }
    }

    @Test
    public void notNullTest() {
        assertNotNull(collectionRepository);
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

    @Test
    public void getTestEntityByCollectionIdEmptyTest() {
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        TestEntity test = testService.getTestEntityByCollectionId(1L);
        verify(testRepository, times(1)).findByCollectionId(1L);
        verify(testRepository, times(2)).save(any(TestEntity.class));
        assertNotNull(test);
        assertEquals(10, test.getQuestions().size());
    }

    @Test
    public void getTestEntityByCollectionIdExceptionTest() {
        CollectionEntity collection1 = new CollectionEntity(2L, "title", 1L, new HashSet<>(), user);
        when(collectionRepository.findById(2L)).thenReturn(Optional.of(collection1));

        assertThrows(InsufficientQuestionsException.class, () -> testService.getTestEntityByCollectionId(2L));
    }

    @Test
    public void toDaoTest() {
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        TestDao testDao = testService.toDao(1L);

        assertEquals(collection.getId(), testDao.getCollectionId());
        assertEquals(collection.getSize(), testDao.getQuestions().size());
        testDao.getQuestions().stream().forEach(questionDao -> assertEquals(4, questionDao.getAnswers().size()));
    }

    @Test
    public void toSortedDaoTest() {
        TestEntity test = new TestEntity();
        test.setId(1L);
        test.setCollectionId(1L);
        testService.generateQuestions(test, collection);
        AtomicLong id = new AtomicLong(1L);
        test.getQuestions().forEach(question -> question.setId(id.getAndIncrement()));

        when(testRepository.findByCollectionId(1L)).thenReturn(Optional.of(test));

        TestDao testDao = testService.toSortedDao(1L);
        assertEquals(collection.getId(), testDao.getCollectionId());
        assertEquals(collection.getSize(), testDao.getQuestions().size());
        for (int i = 0; i < 10; i++) {
            assertEquals(4, testDao.getQuestions().get(i).getAnswers().size());
            assertEquals(Long.valueOf(String.valueOf(i+1)), testDao.getQuestions().get(i).getId());
        }
    }

    @Test
    public void testUnwrapValidTest() {
        TestEntity test = new TestEntity();
        assertEquals(test, testService.unwrapTest(Optional.of(test)));
    }

    @Test
    public void testUnwrapInvalidTest() {
        assertThrows(EntityNotFoundException.class, () -> testService.unwrapTest(Optional.empty()));
    }

    @Test
    public void createTestEntityTest() {
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        TestEntity test = testService.createTestEntity(1L);

        verify(testRepository, times(2)).save(test);
        assertEquals(10, test.getQuestions().size());
        test.getQuestions().stream().forEach(question -> assertEquals(4, question.getAnswers().size()));
    }

    @Test
    public void deleteTestTest() {
        TestEntity test = new TestEntity();
        test.setId(1L);
        when(testRepository.findByCollectionId(1L)).thenReturn(Optional.of(test));

        testService.deleteTest(1L);

        verify(testRepository, times(1)).deleteById(1L);
    }

    @Test
    public void generateQuestionsTest() {
        TestEntity test = new TestEntity();
        test.setId(1L);
        test.setCollectionId(1L);

        testService.generateQuestions(test, collection);

        assertNotNull(test);
        assertEquals(1L, test.getId());
        assertEquals(1L, test.getCollectionId());
        assertEquals(10, test.getQuestions().size());
        test.getQuestions().stream().forEach(question -> assertEquals(4, question.getAnswers().size()));
    }

    @Test
    public void collectionObserverDeleteTest() {
        TestEntity test = new TestEntity();
        test.setId(1L);
        test.setCollectionId(1L);

        when(testRepository.findByCollectionId(1L)).thenReturn(Optional.of(test));

        CollectionObserver event = new CollectionObserver(this, 1L);

        testService.onApplicationEvent(event);

        verify(testRepository).deleteById(1L);
    }

    @Test
    public void deleteTestByIdTest() {
        testService.deleteTestById(1L);

        verify(testRepository).deleteById(1L);
    }
}
