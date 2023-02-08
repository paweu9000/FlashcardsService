package com.flashcard.flashback.cardtests;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.data.CardDto;
import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.card.repository.CardRepository;
import com.flashcard.flashback.card.service.CardService;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceTests {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    @Test
    public void getCardByIdTest() {
        CardEntity toReturn = new CardEntity(2L, "Key", "Value", null);
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toReturn));

        assertThrows(RuntimeException.class, () -> cardService.getCardById(3L));
        assertEquals( 2L, cardRepository.findById(2L).get().getId());
        assertEquals("Key", cardRepository.findById(2L).get().getSide());
        assertEquals("Value", cardRepository.findById(2L).get().getValue());
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
}
