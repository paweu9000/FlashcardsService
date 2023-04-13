package com.flashcard.flashback.test;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "questions")
@Entity
class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String question;
    @ElementCollection
    private Set<String> answers;
    @Column
    private String answer;
    @OneToOne
    @JoinColumn(name = "tests.id")
    private TestEntity test;
}
