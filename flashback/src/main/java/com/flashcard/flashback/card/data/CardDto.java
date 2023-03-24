package com.flashcard.flashback.card.data;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CardDto {
    @NotEmpty
    @NotBlank
    @NotNull
    private String side;
    @NotEmpty
    @NotBlank
    @NotNull
    private String value;
    @NotNull
    private Long collectionId;
    @NotNull
    private Long creatorId;
}
