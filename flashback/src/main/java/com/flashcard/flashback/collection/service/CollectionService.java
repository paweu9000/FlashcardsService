package com.flashcard.flashback.collection.service;

import com.flashcard.flashback.collection.repository.CollectionRepository;
import org.springframework.stereotype.Service;

@Service
public record CollectionService(CollectionRepository collectionRepository) {
}
