package com.flashcard.flashback.collection.entity;

import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.user.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Indexed
@Table(name = "collection")
@AllArgsConstructor
@NoArgsConstructor
public class CollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String title;
    private Long likes;
    @OneToMany(mappedBy = "collector", cascade = CascadeType.ALL)
    private List<CardEntity> cards = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "users.id")
    private UsersEntity owners;

    public void addCard(CardEntity card) {
        cards.add(card);
    }
}
