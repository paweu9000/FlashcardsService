package com.flashcard.flashback.card.entity;

import com.flashcard.flashback.collection.entity.CollectionEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cards")
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String side;

    @NotBlank
    @NotNull
    private String value;

    @ManyToOne
    @JoinColumn(name = "collection.id")
    private CollectionEntity collector;

}
