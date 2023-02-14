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
        if(card == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(cardService.toDao(card), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> editCard(@PathVariable Long id, @RequestBody CardDao cardDao) {
        cardService.editCard(cardDao);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
