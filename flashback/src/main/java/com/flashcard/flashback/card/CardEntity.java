package com.flashcard.flashback.card;

import com.flashcard.flashback.collection.CollectionEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "cards")
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String key;

    @NotBlank
    @NotNull
    private String value;

    @ManyToOne
    @JoinColumn(name = "collection_entity_id")
    private CollectionEntity collectionEntity;

}
