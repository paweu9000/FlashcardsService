package com.flashcard.flashback.card.data;

import com.flashcard.flashback.card.entity.CardEntity;
import lombok.Data;

@Data
public class CardDao {
    private Long id;
    private String side;
    private String value;
    private Long collector;

    public CardDao(CardEntity card) {
        this.id = card.getId();
        this.side = card.getSide();
        this.value = card.getValue();
        this.collector = card.getCollector().getId();
    }
}
