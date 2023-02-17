package com.flashcard.flashback.collection.controller;

import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.collection.service.CollectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/collection")
public record CollectionController(CollectionService collectionService) {

    @GetMapping("/{id}")
    public ResponseEntity<CollectionDao> getCollection(@PathVariable Long id) {
        CollectionEntity collection = collectionService.findById(id);
        CollectionDao collectionDao = new CollectionDao(collection);
        return new ResponseEntity<>(collectionDao, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCollection(@PathVariable Long id, Authentication authentication) {
        collectionService.deleteIfAllowed(authentication, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}+like")
    public ResponseEntity<HttpStatus> upvoteCollection(@PathVariable Long id) {
        collectionService.upvoteCollection(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
