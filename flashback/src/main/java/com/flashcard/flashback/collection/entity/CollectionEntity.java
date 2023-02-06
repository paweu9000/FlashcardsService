package com.flashcard.flashback.collection.entity;

import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.user.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "collection")
@AllArgsConstructor
@NoArgsConstructor
public class CollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long likes;

    @OneToMany(mappedBy = "collector", cascade = CascadeType.ALL)
    private List<CardEntity> cards = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "users.id")
    private UsersEntity owners;

}
