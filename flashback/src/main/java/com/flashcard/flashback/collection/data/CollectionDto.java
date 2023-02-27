package com.flashcard.flashback.collection.data;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CollectionDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String title;
    private Long likes = 0L;
}
