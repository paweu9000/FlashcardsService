package com.flashcard.flashback.collection.controller;

import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.collection.service.CollectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<HttpStatus> deleteCollection(@PathVariable Long id) {
        collectionService.deleteCollectionById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
