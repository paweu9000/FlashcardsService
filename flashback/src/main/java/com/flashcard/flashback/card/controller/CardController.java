package com.flashcard.flashback.card.controller;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.card.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cards")
public record CardController(CardService cardService) {

    @GetMapping("/{id}")
    public ResponseEntity<CardDao> getCard(@PathVariable Long id) {
        CardEntity card = cardService.getCardById(id);
        return new ResponseEntity<>(cardService.toDao(card), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
