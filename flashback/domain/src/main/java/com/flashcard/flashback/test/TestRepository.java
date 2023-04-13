package com.flashcard.flashback.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface TestRepository extends JpaRepository<TestEntity, Long> {
    Optional<TestEntity> findByCollectionId(Long id);
}
