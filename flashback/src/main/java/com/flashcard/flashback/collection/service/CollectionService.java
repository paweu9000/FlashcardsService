package com.flashcard.flashback.collection.service;

import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.data.CollectionDto;
import com.flashcard.flashback.collection.data.mapper.CollectionMapper;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.collection.repository.CollectionRepository;
import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.exception.SavedCollectionDuplicateException;
import com.flashcard.flashback.exception.UnauthorizedDataCreateException;
import com.flashcard.flashback.exception.UnauthorizedDataDeleteException;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        return CollectionMapper.INSTANCE.entityToDao(collection);
    }

    public CollectionEntity findById(Long id) {
        return exists(collectionRepository.findById(id));
    }

    public CollectionEntity exists(Optional<CollectionEntity> collection) {
        if(collection.isPresent()) {
            return collection.get();
        } else {
            throw new EntityNotFoundException(CollectionEntity.class);
        }
    }

    public void deleteCollectionById(Long id) {
        collectionRepository.deleteById(id);
    }

    public void upvoteCollection(Long id, String emailOrLogin) {
        UsersEntity usersEntity = userService.findByEmailOrLogin(emailOrLogin);
        usersEntity.getSavedCollections().forEach(collection -> isUnique(id, collection));
        CollectionEntity collection = findById(id);
        collection.setLikes(collection.getLikes() + 1);
        usersEntity.saveCollection(collection);
        userService.save(usersEntity);
    }

    public void isUnique(Long id, CollectionEntity collection) {
        if(Objects.equals(collection.getId(), id)) throw new SavedCollectionDuplicateException(id);
    }

    public void deleteIfAllowed(String name, Long id) {
        CollectionEntity collection = findById(id);
        String email = collection.getOwners().getEmail();
        String login = collection.getOwners().getLogin();
        if(email.equals(name) || login.equals(name)) deleteCollectionById(id);
        else throw new UnauthorizedDataDeleteException(CollectionEntity.class);
    }

    public String createCollection(String loginOrEmail, CollectionDto collectionDto) {
        if(loginOrEmail == null)
            throw new UnauthorizedDataCreateException(CollectionEntity.class);
        UsersEntity usersEntity = userService.findByEmailOrLogin(loginOrEmail);
        CollectionEntity collection = mapDto(collectionDto);
        collection.setOwners(usersEntity);
        save(collection);
        usersEntity.addCollection(collection);
        userService.save(usersEntity);
        return collection.getId() + " " + usersEntity.getId();
    }

    public CollectionEntity mapDto(CollectionDto collectionDto) {
        return CollectionMapper.INSTANCE.dtoToEntity(collectionDto);
    }

    public void save(CollectionEntity collection) {
        collectionRepository.save(collection);
    }

    public List<CollectionDao> findCollections(String title) {
        List<CollectionEntity> collectionEntities = collectionRepository.findAll();
        if(collectionEntities.isEmpty()) throw new EntityNotFoundException(List.class);
        return collectionEntities.stream().filter(collection -> collection.getTitle().contains(title)).map(this::toDao).toList();
    }

    public List<CollectionDao> findPersonalCollections(String username) {
        UsersEntity user = userService.findByEmailOrLogin(username);
        return user.getCollections().stream().map(this::toDao).toList();
    }
}
