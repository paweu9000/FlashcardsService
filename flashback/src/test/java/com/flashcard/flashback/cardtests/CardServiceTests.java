package com.flashcard.flashback.cardtests;

import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.card.repository.CardRepository;
import com.flashcard.flashback.card.service.CardService;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceTests {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    @Test
    private void getCardByIdTest() {
        CollectionEntity collection = new CollectionEntity();
        collection.setId(31L);
        CardEntity toReturn = new CardEntity(2L, "Key", "Value", collection);
        String errorMessage = "There is no such card!";
        when(cardService.getCardById(2L)).thenReturn(toReturn);
        when(cardService.getCardById(3L)).thenThrow(new RuntimeException(errorMessage));

        assertThrows(RuntimeException.class, () -> cardService.getCardById(3L));
        assertEquals( 2L, cardService.getCardById(2L).getId());
        assertEquals("Key", cardService.getCardById(2L).getSide());
        assertEquals("Value", cardService.getCardById(2L).getValue());
        assertEquals(31L, cardService.getCardById(2L).getCollector().getId());
    }
}
