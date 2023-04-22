package com.flashcard.flashback.collection;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CollectionObserver extends ApplicationEvent {

    private Long collectionId;

    public CollectionObserver(Object source, Long collectionId) {
        super(source);
        this.collectionId = collectionId;
    }
}
