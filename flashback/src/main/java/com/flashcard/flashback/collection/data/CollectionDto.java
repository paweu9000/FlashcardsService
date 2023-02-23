package com.flashcard.flashback.collection.data;

import lombok.Data;

@Data
public class CollectionDto {
    private String title;
    private Long likes = 0L;
}
