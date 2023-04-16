package com.flashcard.flashback.collection;

import com.flashcard.flashback.card.CardEntity;
import com.flashcard.flashback.user.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    private Set<CardEntity> cards = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "users.id")
    private UsersEntity owners;

    public void addCard(CardEntity card) {
        cards.add(card);
    }

    public int getSize() {
        return cards.size();
    }
}
