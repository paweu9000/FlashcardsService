package com.flashcard.flashback.collection;

import com.flashcard.flashback.card.CardEntity;
import com.flashcard.flashback.user.UsersEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class CollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long likes;

    @OneToMany(mappedBy = "collectionEntity", cascade = CascadeType.ALL)
    private List<CardEntity> cards;

    @ManyToOne
    @JoinColumn(name = "users_entity_id")
    private UsersEntity usersEntity;

}
