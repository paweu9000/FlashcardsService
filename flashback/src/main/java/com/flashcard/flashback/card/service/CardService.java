package com.flashcard.flashback.card.service;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.data.CardDto;
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

    public CardDao toDao(CardEntity card) {
        return new CardDao(card);
    }

    public CardEntity mapDto(CardDto cardDto) {
        CardEntity card = new CardEntity();
        card.setValue(cardDto.getValue());
        card.setSide(cardDto.getSide());
        return card;
    }

    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }

    public void editCard(CardDao cardDao) {
        CardEntity card = getCardById(cardDao.getId());
        card.setValue(cardDao.getValue());
        card.setSide(cardDao.getSide());
        cardRepository.save(card);
    }
}
