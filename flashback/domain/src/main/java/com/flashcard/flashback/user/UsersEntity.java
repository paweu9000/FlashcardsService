package com.flashcard.flashback.user;

import com.flashcard.flashback.card.CardEntity;
import com.flashcard.flashback.collection.CollectionEntity;
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
@Table(name = "users")
@NoArgsConstructor
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String login;
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    @OneToMany(mappedBy = "owners", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CollectionEntity> collections = new HashSet<>();
    @Column
    @ElementCollection(targetClass = CollectionEntity.class)
    private Set<CollectionEntity> savedCollections = new HashSet<>();
    @Column
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CardEntity> cards = new HashSet<>();
    @Column
    private boolean isVerified = false;

    public UsersEntity(String login, String username, String email, String password) {
        this.login = login;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void addCollection(CollectionEntity collection) {
        collections.add(collection);
    }
    public void saveCollection(CollectionEntity collection) {
        savedCollections.add(collection);
    }
}
