package com.flashcard.flashback.card.service;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.data.CardDto;
import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.card.repository.CardRepository;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.collection.service.CollectionService;
import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.exception.UnauthorizedDataCreateException;
import com.flashcard.flashback.exception.UnauthorizedDataDeleteException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private CollectionService collectionService;

    public CardService(CardRepository cardRepository, CollectionService collectionService) {
        this.cardRepository = cardRepository;
        this.collectionService = collectionService;
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
        return new CardDao(card);
    }

    public CardEntity mapDto(CardDto cardDto) {
        CardEntity card = new CardEntity();
        card.setValue(cardDto.getValue());
        card.setSide(cardDto.getSide());
        return card;
    }

    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }

    public void editCard(CardDao cardDao) {
        CardEntity card = getCardById(cardDao.getId());
        card.setValue(cardDao.getValue());
        card.setSide(cardDao.getSide());
        cardRepository.save(card);
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

    public void createCard(String loginOrEmail, Long collectionId, CardDto cardDto) {
        CollectionEntity collection = getCollectionIfActionIsAllowed(loginOrEmail, collectionId);
        CardEntity card = mapDto(cardDto);
        card.setCollector(collection);
        card.setCreatedBy(collection.getOwners());
        collection.addCard(card);
        collectionService.save(collection);
    }

    public List<CardDao> getAllCards(Long id) {
        CollectionEntity collection = collectionService.findById(id);
        return collection.getCards().stream().map(this::toDao).toList();
    }
}
