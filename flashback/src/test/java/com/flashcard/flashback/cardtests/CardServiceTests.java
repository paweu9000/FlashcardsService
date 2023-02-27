package com.flashcard.flashback.cardtests;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.data.CardDto;
import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.card.repository.CardRepository;
import com.flashcard.flashback.card.service.CardService;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.collection.repository.CollectionRepository;
import com.flashcard.flashback.collection.service.CollectionService;
import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.exception.UnauthorizedDataCreateException;
import com.flashcard.flashback.exception.UnauthorizedDataDeleteException;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.repository.UserRepository;
import com.flashcard.flashback.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceTests {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private CollectionRepository collectionRepository;

    @InjectMocks
    @Spy
    private CollectionService collectionService;

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    @Spy
    private UserService userService;

    @InjectMocks
    private CardService cardService;
    private CardEntity card;
    private CollectionEntity collection;
    private UsersEntity user;

    @Before
    public void setUp() {
        userService = new UserService(userRepository, null);
        cardService.setCollectionService(collectionService);
        cardService.setUserService(userService);
        user = new UsersEntity("login", "username", "email@example.com", "password");
        collection = new CollectionEntity(1L, "title", 0L, new ArrayList<>(), user);
        card = new CardEntity();
        card.setId(1L);
        card.setValue("Value");
        card.setSide("Side");
        card.setCreatedBy(user);
    }

    @Test
    public void getCardByIdTest() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        assertThrows(EntityNotFoundException.class, () -> cardService.getCardById(3L));
        assertEquals( 1L, cardService.getCardById(1L).getId());
        assertEquals("Side", cardService.getCardById(1L).getSide());
        assertEquals("Value", cardService.getCardById(1L).getValue());
    }

    @Test
    public void toDaoTest() {
        card.setCollector(collection);
        CardDao cardDao = cardService.toDao(card);

        assertEquals(cardDao.getId(), card.getId());
        assertEquals(cardDao.getSide(), card.getSide());
        assertEquals(card.getValue(), card.getValue());
    }

    @Test
    public void mapDtoTest() {
        CardDto cardDto = new CardDto();
        cardDto.setSide("Side");
        cardDto.setValue("Value");
        cardDto.setCollectionId(1L);
        cardDto.setCreatorId(1L);
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        CardEntity card = cardService.mapDto(cardDto);

        assertEquals(card.getValue(), cardDto.getValue());
        assertEquals(card.getSide(), cardDto.getSide());
    }

    @Test
    public void deleteCardTest() {
        Long id = 3L;
        cardService.deleteCard(id);
        verify(cardRepository).deleteById(3L);
    }

    @Test
    public void editCardTest() {
        card.setCollector(collection);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        CardDao cardDao = new CardDao(card);
        cardDao.setSide("Changed side");
        cardDao.setValue("Changed value");
        cardService.editCard(cardDao, "login");

        assertEquals(cardDao.getSide(), cardService.getCardById(1L).getSide());
        assertEquals(cardDao.getValue(), cardService.getCardById(1L).getValue());
    }

    @Test
    public void deleteIfAllowedValidTest() {
        when(authentication.getName()).thenReturn("email@example.com");
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        assertDoesNotThrow(() -> cardService.deleteIfAllowed(authentication, 1L));
    }

    @Test
    public void deleteIfAllowedThrowExceptionTest() {
        when(authentication.getName()).thenReturn("invalid login");
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        assertThrows(UnauthorizedDataDeleteException.class, () -> cardService.deleteIfAllowed(authentication, 1L));
    }

    @Test
    public void getCollectionIfActionIsAllowedValidTest() {
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        CollectionEntity returned = cardService.getCollectionIfActionIsAllowed("login", 1L);

        assertEquals(returned.getOwners(), collection.getOwners());
    }

    @Test
    public void getCollectionIfActionIsAllowedInvalidTest() {
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        assertThrows(UnauthorizedDataCreateException.class, () ->
                cardService.getCollectionIfActionIsAllowed("invalid_login", 1L));
    }

    @Test
    public void createCardTest() {
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        CardDto cardDto = new CardDto();
        cardDto.setSide("Side");
        cardDto.setValue("Value");
        cardDto.setCollectionId(1L);
        cardDto.setCreatorId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        cardService.createCard("login", 1L, cardDto);

        verify(collectionRepository).save(collection);
    }

    @Test
    public void getAllCardsTest() {
        CardEntity anotherCard = new CardEntity(32L, "Side", "Value", collection, user);
        card.setCollector(collection);
        collection.addCard(card);
        collection.addCard(anotherCard);
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        List<CardDao> cards = cardService.getAllCards(1L);

        assertEquals(card.getId(), cards.get(0).getId());
        assertEquals(card.getValue(), cards.get(0).getValue());
        assertEquals(card.getSide(), cards.get(0).getSide());
        assertEquals(anotherCard.getId(), cards.get(1).getId());
        assertEquals(anotherCard.getValue(), cards.get(1).getValue());
        assertEquals(anotherCard.getSide(), cards.get(1).getSide());
    }

    @Test
    public void getAllCardsEntityNotFoundTest() {
        assertThrows(EntityNotFoundException.class, () -> cardService.getAllCards(1L));
    }
}
