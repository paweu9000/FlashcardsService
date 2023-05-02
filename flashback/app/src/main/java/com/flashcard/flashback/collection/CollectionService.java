package com.flashcard.flashback.collection;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.data.CollectionDto;
import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.exception.SavedCollectionDuplicateException;
import com.flashcard.flashback.exception.UnauthorizedDataCreateException;
import com.flashcard.flashback.exception.UnauthorizedDataDeleteException;
import com.flashcard.flashback.user.UserService;
import com.flashcard.flashback.user.UsersEntity;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
public class CollectionService{

    @PersistenceContext
    EntityManager entityManager;

    CollectionRepository collectionRepository;
    UserService userService;
    ApplicationEventPublisher eventPublisher;

    CollectionService(CollectionRepository collectionRepository, UserService userService, ApplicationEventPublisher eventPublisher) {
        this.collectionRepository = collectionRepository;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    void setUserService(UserService userService) {
        this.userService = userService;
    }

    CollectionDao toDao(CollectionEntity collection) {
        return CollectionMapper.INSTANCE.entityToDao(collection);
    }

    public CollectionEntity findById(Long id) {
        return exists(collectionRepository.findById(id));
    }

    CollectionEntity exists(Optional<CollectionEntity> collection) {
        return collection.orElseThrow(() -> new EntityNotFoundException(CollectionEntity.class));
    }

    void deleteCollectionById(Long id) {
        collectionRepository.deleteById(id);
        eventPublisher.publishEvent(new CollectionObserver(this, id));
    }

    void upvoteCollection(Long id, String emailOrLogin) {
        UsersEntity usersEntity = userService.findByEmailOrLogin(emailOrLogin);
        usersEntity.getSavedCollections().forEach(collection -> isUnique(id, collection));
        CollectionEntity collection = findById(id);
        collection.setLikes(collection.getLikes() + 1);
        usersEntity.saveCollection(collection);
        userService.save(usersEntity);
    }

    void isUnique(Long id, CollectionEntity collection) {
        if(Objects.equals(collection.getId(), id)) throw new SavedCollectionDuplicateException(id);
    }

    void deleteIfAllowed(String name, Long id) {
        CollectionEntity collection = findById(id);
        if(determineOwner(collection.getOwners(), name)) deleteCollectionById(id);
        else throw new UnauthorizedDataDeleteException(CollectionEntity.class);
    }

    boolean determineOwner(UsersEntity user, String owner) {
        return user.getEmail().equals(owner) || user.getLogin().equals(owner);
    }

    String createCollection(String loginOrEmail, CollectionDto collectionDto) {
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

    CollectionEntity mapDto(CollectionDto collectionDto) {
        return CollectionMapper.INSTANCE.dtoToEntity(collectionDto);
    }

    public void save(CollectionEntity collection) {
        collectionRepository.save(collection);
    }

    List<CollectionDao> findPersonalCollections(UsersEntity user) {
        return toSortedCollectionListDao(new ArrayList<>(user.getCollections().stream().map(this::toDao).toList()));
    }

    List<CollectionDao> findPersonalSavedCollections(UsersEntity user) {
        return toSortedCollectionListDao(new ArrayList<>(user.getSavedCollections().stream().map(this::toDao).toList()));
    }

    List<List<CollectionDao>> getPersonalCollectionData(String username) {
        UsersEntity user = userService.findByEmailOrLogin(username);
        List<List<CollectionDao>> personalCollectionData = new ArrayList<>();
        personalCollectionData.add(findPersonalCollections(user));
        personalCollectionData.add(findPersonalSavedCollections(user));
        return personalCollectionData;
    }

    List<CollectionDao> searchByTitle(String title) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(CollectionEntity.class)
                .get();
        Query luceneQuery = buildLuceneQuery(queryBuilder, title);
        List<CollectionEntity> collections = getCollectionListFromQuery(luceneQuery, fullTextEntityManager);
        return collections.stream().map(this::toDao).toList();
    }

    private List<CollectionEntity> getCollectionListFromQuery(Query query, FullTextEntityManager textEntityManager) {
        return textEntityManager.createFullTextQuery(query, CollectionEntity.class).getResultList();
    }

    private Query buildLuceneQuery(QueryBuilder queryBuilder, String title) {
        return queryBuilder
                .keyword()
                .fuzzy()
                .onField("title")
                .matching(title)
                .createQuery();
    }

    CollectionDao toSortedDao(CollectionEntity collection) {

        CollectionDao collectionDao = toDao(collection);
        collectionDao.setCards(collectionDao.getCards().stream().sorted(Comparator.comparingLong(CardDao::getId)).toList());
        return collectionDao;
    }

    List<CollectionDao> toSortedCollectionListDao(List<CollectionDao> collections) {

        return collections.stream().sorted(Comparator.comparingLong(CollectionDao::getId)).toList();
    }
}
