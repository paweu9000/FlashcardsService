package com.flashcard.flashback.card.controller;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.data.CardDto;
import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.card.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/cards")
public record CardController(CardService cardService) {

    @GetMapping("/{id}")
    public ResponseEntity<CardDao> getCard(@PathVariable Long id) {
        CardEntity card = cardService.getCardById(id);
        return new ResponseEntity<>(cardService.toDao(card), HttpStatus.OK);
    }

    @PostMapping("/{collectionId}")
    public ResponseEntity<HttpStatus> postCard(@PathVariable(name = "collectionId") Long collectionId,
                                               @Valid @RequestBody CardDto cardDto,
                                               @CurrentSecurityContext(expression = "authentication?.name") String name) {
        cardService.createCard(name, collectionId, cardDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{collectionId}")
    public ResponseEntity<HttpStatus> postCards(@PathVariable(name = "collectionId") Long collectionId,
                                               @Valid @RequestBody List<CardDto> cardDto,
                                               @CurrentSecurityContext(expression = "authentication?.name") String name) {
        cardService.createCards(name, collectionId, cardDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCard(@PathVariable Long id, Authentication authentication) {
        cardService.deleteIfAllowed(authentication, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> editCard(@PathVariable Long id,
                                               @RequestBody CardDao cardDao,
                                               @CurrentSecurityContext(expression = "authentication?.name") String name) {
        cardService.editCard(cardDao, name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all?={collectionId}")
    public ResponseEntity<List<CardDao>> getAllCardsFromCollection(@PathVariable Long collectionId) {
        List<CardDao> cards = cardService.getAllCards(collectionId);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }
}
