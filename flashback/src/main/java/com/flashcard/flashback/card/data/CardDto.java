package com.flashcard.flashback.card.data;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CardDto {
    @NotEmpty
    @NotEmpty
    @NotNull
    private String side;
    @NotEmpty
    @NotEmpty
    @NotNull
    private String value;
    @NotNull
    private Long collectionId;
    @NotNull
    private Long creatorId;
}
