package com.flashcard.flashback.user;

import com.flashcard.flashback.collection.CollectionEntity;
import com.flashcard.flashback.user.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Min(6)
    @Max(30)
    private String login;

    @NotNull
    @NotBlank
    @Min(6)
    @Max(30)
    private String username;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Min(6)
    @Max(30)
    private String password;

    private Role role = Role.USER;

    @OneToMany(mappedBy = "usersEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<CollectionEntity> collection = new ArrayList<>();

    @Column
    List<Long> savedCollections = new ArrayList<>();
}
