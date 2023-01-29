package com.flashcard.flashback.card.service;

import com.flashcard.flashback.card.repository.CardRepository;
import org.springframework.stereotype.Service;

@Service
public record CardService(CardRepository cardRepository) {
}
