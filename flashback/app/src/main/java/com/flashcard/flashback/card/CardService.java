package com.flashcard.flashback.card;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.data.CardDto;
import com.flashcard.flashback.collection.CollectionEntity;
import com.flashcard.flashback.collection.CollectionObserver;
import com.flashcard.flashback.collection.CollectionService;
import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.exception.UnauthorizedDataAccessException;
import com.flashcard.flashback.exception.UnauthorizedDataCreateException;
import com.flashcard.flashback.exception.UnauthorizedDataDeleteException;
import com.flashcard.flashback.user.UserService;
import com.flashcard.flashback.user.UsersEntity;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
class CardService {
    final CardRepository cardRepository;
    CollectionService collectionService;
    UserService userService;
    ApplicationEventPublisher eventPublisher;

    CardService(CardRepository cardRepository, CollectionService collectionService,
                UserService userService, ApplicationEventPublisher eventPublisher) {
        this.cardRepository = cardRepository;
        this.collectionService = collectionService;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    void setUserService(UserService userService) {
        this.userService = userService;
    }

    void setCollectionService(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    CardEntity getCardById(Long id) throws EntityNotFoundException{
        return cardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, CardEntity.class));
    }

    CardDao toDao(CardEntity card) {
        return CardMapper.INSTANCE.entityToDao(card);
    }

    CardEntity mapDto(CardDto cardDto) {
        return CardMapper.INSTANCE.toCardEntity(cardDto, this.userService, this.collectionService);
    }

    void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }

    boolean editIfAllowed(String loginOrEmail, UsersEntity user) {
        return user.getEmail().equals(loginOrEmail) || user.getLogin().equals(loginOrEmail);
    }
    void editCard(CardDao cardDao, String loginOrEmail) {
        CardEntity card = getCardById(cardDao.getId());
        if(editIfAllowed(loginOrEmail, card.getCreatedBy())) {
            card.setValue(cardDao.getValue());
            card.setSide(cardDao.getSide());
            cardRepository.save(card);
            eventPublisher.publishEvent(new CollectionObserver(this, card.getCollector().getId()));
        } else {
            throw new UnauthorizedDataAccessException(CardEntity.class);
        }
    }

    void deleteIfAllowed(Authentication authentication, Long id) {
        CardEntity card = getCardById(id);
        String loginOrEmail = authentication.getName();
        if (determineOwner(card.getCreatedBy(), loginOrEmail)) {
            deleteCard(id);
            eventPublisher.publishEvent(new CollectionObserver(this, card.getCollector().getId()));
        }
        else throw new UnauthorizedDataDeleteException(CardEntity.class);
    }

    boolean determineOwner(UsersEntity user, String owner) {
        return user.getEmail().equals(owner) || user.getLogin().equals(owner);
    }

    CollectionEntity getCollectionIfActionIsAllowed(String loginOrEmail, Long collectionId) {
        if(loginOrEmail == null)
            throw new UnauthorizedDataCreateException(CardEntity.class);
        CollectionEntity collection = collectionService.findById(collectionId);
        if(!determineOwner(collection.getOwners(), loginOrEmail))
            throw new UnauthorizedDataCreateException(CardEntity.class);
        return collection;
    }

    CardDao addCard(String loginOrEmail, Long collectionId, CardDto cardDto) {
        eventPublisher.publishEvent(new CollectionObserver(this, collectionId));
        return createCard(loginOrEmail, collectionId, cardDto);
    }

    CardDao createCard(String loginOrEmail, Long collectionId, CardDto cardDto) {
        CollectionEntity collection = getCollectionIfActionIsAllowed(loginOrEmail, collectionId);
        CardEntity card = mapDto(cardDto);
        card.setCollector(collection);
        card.setCreatedBy(collection.getOwners());
        collection.addCard(card);
        collectionService.save(collection);
        return toDao(card);
    }

    void createCards(String loginOrEmail, Long collectionId, List<CardDto> cards) {
        cards.forEach(cardDto -> createCard(loginOrEmail, collectionId, cardDto));
    }

    Set<CardDao> getAllCards(Long id) {
        CollectionEntity collection = collectionService.findById(id);
        return collection.getCards().stream().map(this::toDao).collect(Collectors.toSet());
    }
}
