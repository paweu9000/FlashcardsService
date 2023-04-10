package com.flashcard.flashback.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, Long> {
    Optional<UsersEntity> findByEmail(String email);
    Optional<UsersEntity> findByLogin(String login);
    Optional<UsersEntity> findByUsername(String username);
}
