package com.flashcard.flashback.test;

import com.flashcard.flashback.collection.CollectionEntity;
import com.flashcard.flashback.collection.CollectionService;
import com.flashcard.flashback.exception.InsufficientQuestionsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class TestService {
    final TestRepository repository;
    final CollectionService collectionService;

    public TestService(TestRepository repository, CollectionService collectionService) {
        this.repository = repository;
        this.collectionService = collectionService;
    }

    TestEntity getTestEntityByCollectionId(Long collectionId) {
        Optional<TestEntity> testEntity = repository.findByCollectionId(collectionId);
        return testEntity.orElseGet(() -> createTestEntity(collectionId));
    }

    TestEntity createTestEntity(Long collectionId) {
        CollectionEntity collection = collectionService.findById(collectionId);
        if (collection.getSize() < 4) throw new InsufficientQuestionsException(collectionId);
        return null;
    }
}
