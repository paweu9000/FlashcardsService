package com.flashcard.flashback.card.controller;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.card.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cards")
public record CardController(CardService cardService) {

    @GetMapping("/{id}")
    public ResponseEntity<CardDao> getCard(@PathVariable Long id) {
        CardEntity card = cardService.getCardById(id);
        CardDao cardDao = new CardDao(card);
        return new ResponseEntity<>(cardDao, HttpStatus.OK);
    }
}
