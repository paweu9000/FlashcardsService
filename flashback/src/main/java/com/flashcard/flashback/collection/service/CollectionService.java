package com.flashcard.flashback.collection.service;

import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.data.CollectionDto;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.collection.repository.CollectionRepository;
import com.flashcard.flashback.exception.UnauthorizedDataCreateException;
import com.flashcard.flashback.exception.UnauthorizedDataDeleteException;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CollectionService{

    CollectionRepository collectionRepository;
    UserService userService;

    public CollectionService(CollectionRepository collectionRepository, UserService userService) {
        this.collectionRepository = collectionRepository;
        this.userService = userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

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

    public void createCollection(String loginOrEmail, CollectionDto collectionDto) {
        if(loginOrEmail == null)
            throw new UnauthorizedDataCreateException(CollectionEntity.class);
        CollectionEntity collection = new CollectionEntity();
        UsersEntity usersEntity = userService.findByEmailOrLogin(loginOrEmail);
        collection.setLikes(collectionDto.getLikes());
        collection.setOwners(usersEntity);
        usersEntity.addCollection(collection);
        userService.save(usersEntity);
    }
}
