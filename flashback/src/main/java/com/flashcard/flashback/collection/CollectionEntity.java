package com.flashcard.flashback.collection;

import com.flashcard.flashback.card.CardEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "cards")
public class CollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long likes;

    @OneToMany(mappedBy = "collection")
    private List<CardEntity> cards;
}
