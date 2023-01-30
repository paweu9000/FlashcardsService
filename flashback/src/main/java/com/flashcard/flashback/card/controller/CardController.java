package com.flashcard.flashback.card.controller;

import com.flashcard.flashback.card.service.CardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cards")
public record CardController(CardService cardService) {
}
