package com.flashcard.flashback.card.service;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.data.CardDto;
import com.flashcard.flashback.card.data.mapper.CardMapper;
import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.card.repository.CardRepository;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.collection.service.CollectionService;
import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.exception.UnauthorizedDataAccessException;
import com.flashcard.flashback.exception.UnauthorizedDataCreateException;
import com.flashcard.flashback.exception.UnauthorizedDataDeleteException;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private CollectionService collectionService;
    private UserService userService;

    CardService(CardRepository cardRepository, CollectionService collectionService, UserService userService) {
        this.cardRepository = cardRepository;
        this.collectionService = collectionService;
        this.userService = userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setCollectionService(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    public CardEntity getCardById(Long id) throws EntityNotFoundException{
        Optional<CardEntity> card = cardRepository.findById(id);
        if (card.isPresent()) {
            return card.get();
        } else {
            throw new EntityNotFoundException(id, CardEntity.class);
        }
    }

    public CardDao toDao(CardEntity card) {
        return CardMapper.INSTANCE.entityToDao(card);
    }

    public CardEntity mapDto(CardDto cardDto) {
        return CardMapper.INSTANCE.toCardEntity(cardDto, this.userService, this.collectionService);
    }

    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }

    private boolean editIfAllowed(String loginOrEmail, UsersEntity user) {
        return user.getEmail().equals(loginOrEmail) || user.getLogin().equals(loginOrEmail);
    }
    public void editCard(CardDao cardDao, String loginOrEmail) {
        CardEntity card = getCardById(cardDao.getId());
        if(editIfAllowed(loginOrEmail, card.getCreatedBy())) {
            card.setValue(cardDao.getValue());
            card.setSide(cardDao.getSide());
            cardRepository.save(card);
        } else {
            throw new UnauthorizedDataAccessException(CardEntity.class);
        }
    }

    public void deleteIfAllowed(Authentication authentication, Long id) {
        CardEntity card = getCardById(id);
        String loginOrEmail = authentication.getName();
        String login = card.getCreatedBy().getLogin();
        String email = card.getCreatedBy().getEmail();
        if (login.equals(loginOrEmail) || email.equals(loginOrEmail)) deleteCard(id);
        else throw new UnauthorizedDataDeleteException(CardEntity.class);
    }

    public CollectionEntity getCollectionIfActionIsAllowed(String loginOrEmail, Long collectionId) {
        if(loginOrEmail == null)
            throw new UnauthorizedDataCreateException(CardEntity.class);
        CollectionEntity collection = collectionService.findById(collectionId);
        if(!collection.getOwners().getEmail()
                .equals(loginOrEmail) &&  !collection
                .getOwners().getLogin().equals(loginOrEmail))
            throw new UnauthorizedDataCreateException(CardEntity.class);
        return collection;
    }

    public CardDao createCard(String loginOrEmail, Long collectionId, CardDto cardDto) {
        CollectionEntity collection = getCollectionIfActionIsAllowed(loginOrEmail, collectionId);
        CardEntity card = mapDto(cardDto);
        card.setCollector(collection);
        card.setCreatedBy(collection.getOwners());
        collection.addCard(card);
        collectionService.save(collection);
        return toDao(card);
    }

    public void createCards(String loginOrEmail, Long collectionId, List<CardDto> cards) {
        cards.forEach(cardDto -> createCard(loginOrEmail, collectionId, cardDto));
    }

    public List<CardDao> getAllCards(Long id) {
        CollectionEntity collection = collectionService.findById(id);
        return collection.getCards().stream().map(this::toDao).toList();
    }
}
