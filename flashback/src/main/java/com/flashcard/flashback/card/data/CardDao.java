package com.flashcard.flashback.card.data;

import com.flashcard.flashback.card.entity.CardEntity;
import lombok.Data;

@Data
public class CardDao {

    private String side;
    private String value;

    public CardDao(CardEntity card) {
        this.side = card.getSide();
        this.value = card.getValue();
    }
}
