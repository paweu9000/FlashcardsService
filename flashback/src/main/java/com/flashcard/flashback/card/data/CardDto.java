package com.flashcard.flashback.card.data;

import lombok.Data;

@Data
public class CardDto {

    private String side;
    private String value;
    private Long collectionId;
    private Long creatorId;
}
