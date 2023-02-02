package com.flashcard.flashback.card.service;

import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.card.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardService {
    final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public CardEntity getCardById(Long id) {
        Optional<CardEntity> card = cardRepository.findById(id);
        if (card.isPresent()) {
            return card.get();
        } else {
            throw new RuntimeException("There is no such card!");
        }
    }
}
