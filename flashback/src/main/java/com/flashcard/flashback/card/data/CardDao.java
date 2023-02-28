package com.flashcard.flashback.card.data;

import lombok.Data;

@Data
public class CardDao {
    private Long id;
    private String side;
    private String value;
    private Long collector;
    private Long createdBy;
}
