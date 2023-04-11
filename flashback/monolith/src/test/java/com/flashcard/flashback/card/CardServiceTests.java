package com.flashcard.flashback.card;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.data.CardDto;
import com.flashcard.flashback.collection.CollectionEntity;
import com.flashcard.flashback.collection.CollectionRepository;
import com.flashcard.flashback.collection.CollectionService;
import com.flashcard.flashback.email.EmailService;
import com.flashcard.flashback.email.VerificationTokenService;
import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.exception.UnauthorizedDataCreateException;
import com.flashcard.flashback.exception.UnauthorizedDataDeleteException;
import com.flashcard.flashback.user.UserRepository;
import com.flashcard.flashback.user.UserService;
import com.flashcard.flashback.user.UsersEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    @MockBean
    private CollectionService collectionService;
    @Mock
    VerificationTokenService verificationTokenService;
    @Mock
    EmailService emailService;

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @InjectMocks
    private CardService cardService;
    private CardEntity card;
    private CollectionEntity collection;
    private UsersEntity user;

    @Before
    public void setUp() {
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
    public void mockNotNull() {
        assertNotNull(userRepository);
        assertNotNull(emailService);
        assertNotNull(userService);
        assertNotNull(cardService);
        assertNotNull(cardRepository);
        assertNotNull(authentication);
        assertNotNull(collectionRepository);
        assertNotNull(collectionService);
    }

    @Test
    public void getCardByIdTest() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        assertNotNull(cardService.getCardById(1L));
        assertThrows(EntityNotFoundException.class, () -> cardService.getCardById(3L));
        assertEquals( 1L, cardService.getCardById(1L).getId());
        assertEquals("Side", cardService.getCardById(1L).getSide());
        assertEquals("Value", cardService.getCardById(1L).getValue());
    }

    @Test
    public void toDaoTest() {
        card.setCollector(collection);
        CardDao cardDao = cardService.toDao(card);

        assertNotNull(cardDao);
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

        assertNotNull(card);
        assertDoesNotThrow(() -> userRepository.findById(1L));
        assertDoesNotThrow(() -> collectionRepository.findById(1L));
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
        CardDao cardDao = cardService.toDao(card);
        cardDao.setSide("Changed side");
        cardDao.setValue("Changed value");
        cardService.editCard(cardDao, "login");

        assertDoesNotThrow(() -> cardRepository.findById(1L));
        assertNotNull(cardDao);
        assertEquals(cardDao.getSide(), cardService.getCardById(1L).getSide());
        assertEquals(cardDao.getValue(), cardService.getCardById(1L).getValue());
    }

    @Test
    public void deleteIfAllowedValidTest() {
        when(authentication.getName()).thenReturn("email@example.com");
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        assertEquals("email@example.com", authentication.getName());
        assertDoesNotThrow(() -> cardService.deleteIfAllowed(authentication, 1L));
    }

    @Test
    public void deleteIfAllowedThrowExceptionTest() {
        when(authentication.getName()).thenReturn("invalid login");
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        assertEquals("invalid login", authentication.getName());
        assertThrows(UnauthorizedDataDeleteException.class, () -> cardService.deleteIfAllowed(authentication, 1L));
    }

    @Test
    public void getCollectionIfActionIsAllowedValidTest() {
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        CollectionEntity returned = cardService.getCollectionIfActionIsAllowed("login", 1L);

        assertDoesNotThrow(() -> collectionRepository.findById(1L));
        assertNotNull(returned);
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

        assertNotNull(cards);
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
