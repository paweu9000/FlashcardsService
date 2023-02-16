package com.flashcard.flashback.cardtests;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.data.CardDto;
import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.card.repository.CardRepository;
import com.flashcard.flashback.card.service.CardService;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.exception.UnauthorizedDataDeleteException;
import com.flashcard.flashback.user.entity.UsersEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceTests {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CardService cardService;

    @Test
    public void getCardByIdTest() {
        CardEntity toReturn = new CardEntity(2L, "Key", "Value", null, null);
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toReturn));

        assertThrows(RuntimeException.class, () -> cardService.getCardById(3L));
        assertEquals( 2L, cardService.getCardById(2L).getId());
        assertEquals("Key", cardService.getCardById(2L).getSide());
        assertEquals("Value", cardService.getCardById(2L).getValue());
    }

    @Test
    public void toDaoTest() {
        CardEntity card = new CardEntity();
        card.setId(3L);
        card.setSide("Side");
        card.setValue("Value");
        card.setCollector(new CollectionEntity());
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
        CollectionEntity collection = new CollectionEntity(11L, 32L, null, null);
        CardEntity card = new CardEntity(2L, "Side", "Value", collection, null);
        when(cardRepository.findById(2L)).thenReturn(Optional.of(card));
        CardDao cardDao = new CardDao(card);
        cardDao.setSide("Changed side");
        cardDao.setValue("Changed value");
        cardService.editCard(cardDao);

        assertEquals(cardDao.getSide(), cardService.getCardById(2L).getSide());
        assertEquals(cardDao.getValue(), cardService.getCardById(2L).getValue());
    }

    @Test
    public void deleteIfAllowedValidTest() {
        when(authentication.getName()).thenReturn("email@example.com");
        UsersEntity usersEntity = new UsersEntity("login", null, "email@example.com", null);
        CardEntity card = new CardEntity(1L, "side", "value", null, usersEntity);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        assertDoesNotThrow(() -> cardService.deleteIfAllowed(authentication, 1L));
    }
}
