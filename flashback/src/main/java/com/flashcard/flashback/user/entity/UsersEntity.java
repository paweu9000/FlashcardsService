package com.flashcard.flashback.user.entity;

import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.user.role.Role;
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
@Table(name = "users")
@NoArgsConstructor
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String login;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private Role role = Role.USER;
    @Column
    @OneToMany(mappedBy = "owners", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CollectionEntity> collections = new ArrayList<>();
    @Column
    @ElementCollection(targetClass = CollectionEntity.class)
    private List<CollectionEntity> savedCollections = new ArrayList<>();
    @Column
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CardEntity> cards = new ArrayList<>();

    public UsersEntity(String login, String username, String email, String password) {
        this.login = login;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void addCollection(CollectionEntity collection) {
        collections.add(collection);
    }
}
