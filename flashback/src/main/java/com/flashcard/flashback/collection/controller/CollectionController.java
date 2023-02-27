package com.flashcard.flashback.collection.controller;

import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.data.CollectionDto;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.collection.service.CollectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/collection")
public record CollectionController(CollectionService collectionService) {

    @GetMapping("/{id}")
    public ResponseEntity<CollectionDao> getCollection(@PathVariable Long id) {
        CollectionEntity collection = collectionService.findById(id);
        CollectionDao collectionDao = new CollectionDao(collection);
        return new ResponseEntity<>(collectionDao, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> createCollection(@Valid @RequestBody CollectionDto collectionDto,
                                                       @CurrentSecurityContext(expression = "authentication?.name")
                                                       String username) {
        collectionService.createCollection(username, collectionDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCollection(@PathVariable Long id, @CurrentSecurityContext SecurityContext context) {
        collectionService.deleteIfAllowed(context.getAuthentication(), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<HttpStatus> upvoteCollection(@PathVariable Long id, @CurrentSecurityContext
            (expression = "authentication?.name") String username) {
        collectionService.upvoteCollection(id, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search?title={title}")
    public ResponseEntity<List<CollectionDao>> findCollectionsByTitle(@PathVariable String title) {
        List<CollectionDao> collections = collectionService.findCollections(title);
        return new ResponseEntity<>(collections, HttpStatus.OK);
    }
}
