package com.flashcard.flashback.collection.data;

import com.flashcard.flashback.card.data.CardDao;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CollectionDao {
    private Long id;
    private String title;
    private Long likes;
    private List<CardDao> cards = new ArrayList<>();
    private Long owners;
}
