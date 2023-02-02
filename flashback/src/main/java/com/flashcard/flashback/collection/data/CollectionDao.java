package com.flashcard.flashback.collection.data;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.entity.CardEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CollectionDao {
    private Long id;
    private Long likes;
    private List<CardDao> cards = new ArrayList<>();

    public CollectionDao(Long likes, List<CardEntity> cardEntities) {
        this.likes = likes;
        cardEntities.forEach(card -> this.cards.add(new CardDao(card)));
    }
}
