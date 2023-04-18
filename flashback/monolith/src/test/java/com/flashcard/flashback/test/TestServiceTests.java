package com.flashcard.flashback.test;

import com.flashcard.flashback.collection.CollectionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class TestServiceTests {

    @Mock
    CollectionService collectionService;

    @Mock
    TestRepository testRepository;

    @InjectMocks
    TestService testService;

    @Test
    public void notNullTest() {
        assertNotNull(collectionService);
        assertNotNull(testRepository);
        assertNotNull(testService);
        assertNotNull(testService.collectionService);
        assertNotNull(testService.repository);
    }
}
