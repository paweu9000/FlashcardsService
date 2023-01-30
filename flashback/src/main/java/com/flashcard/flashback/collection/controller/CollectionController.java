package com.flashcard.flashback.collection.controller;

import com.flashcard.flashback.collection.service.CollectionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/collection")
public record CollectionController(CollectionService collectionService) {
}
