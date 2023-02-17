package com.flashcard.flashback.collection.service;

import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.collection.repository.CollectionRepository;
import com.flashcard.flashback.exception.UnauthorizedDataDeleteException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public record CollectionService(CollectionRepository collectionRepository) {

    public CollectionDao toDao(CollectionEntity collection) {
        return new CollectionDao(collection);
    }

    public CollectionEntity findById(Long id) {
        return exists(collectionRepository.findById(id).get());
    }

    public CollectionEntity exists(CollectionEntity collection) {
        if(collection != null) {
            return collection;
        } else {
            throw new RuntimeException("This collection does not exist!");
        }
    }

    public void deleteCollectionById(Long id) {
        collectionRepository.deleteById(id);
    }

    public void upvoteCollection(Long id) {
        CollectionEntity collection = findById(id);
        collection.setLikes(collection.getLikes() + 1);
        collectionRepository.save(collection);
    }

    public void deleteIfAllowed(Authentication authentication, Long id) {
        CollectionEntity collection = findById(id);
        String emailOrLogin = authentication.getName();
        String email = collection.getOwners().getEmail();
        String login = collection.getOwners().getLogin();
        if(email.equals(emailOrLogin) || login.equals(emailOrLogin)) deleteCollectionById(id);
        else throw new UnauthorizedDataDeleteException(CollectionEntity.class);
    }
}
