package com.flashcard.flashback.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CardRepository extends JpaRepository<CardEntity, Long> {
}
