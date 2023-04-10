package com.flashcard.flashback.card;

import com.flashcard.flashback.collection.CollectionEntity;
import com.flashcard.flashback.user.UsersEntity;
import lombok.*;

import javax.persistence.*;

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
    private String side;
    private String value;
    @ManyToOne
    @JoinColumn(name = "collection.id")
    private CollectionEntity collector;
    @ManyToOne
    @JoinColumn(name = "users.id")
    private UsersEntity createdBy;

}
