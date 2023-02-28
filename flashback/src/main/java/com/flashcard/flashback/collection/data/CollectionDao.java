package com.flashcard.flashback.collection.data;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.data.mapper.CardMapper;
import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CollectionDao {
    private Long id;
    private String title;
    private Long likes;
    private List<CardDao> cards = new ArrayList<>();

    public CollectionDao(CollectionEntity collection) {
        this.title = collection.getTitle();
        this.id = collection.getId();
        this.likes = collection.getLikes();
        collection.getCards().forEach(card -> this.cards.add(CardMapper.INSTANCE.entityToDao(card)));
    }
}
