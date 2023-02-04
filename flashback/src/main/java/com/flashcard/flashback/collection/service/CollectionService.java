package com.flashcard.flashback.collection.service;

import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.collection.repository.CollectionRepository;
import org.springframework.stereotype.Service;

@Service
public record CollectionService(CollectionRepository collectionRepository) {

    public CollectionDao toDao(CollectionEntity collection) {
        return new CollectionDao(collection);
    }
}
