package com.flashcard.flashback.collection;

import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.data.CollectionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/collection")
record CollectionController(CollectionService collectionService) {

    @GetMapping("/{id}")
    ResponseEntity<CollectionDao> getSortedOrderCollection(@PathVariable Long id) {
        CollectionEntity collection = collectionService.findById(id);
        CollectionDao collectionDao = collectionService.toSortedDao(collection);
        return new ResponseEntity<>(collectionDao, HttpStatus.OK);
    }

    @GetMapping("/{id}/random")
    ResponseEntity<CollectionDao> getRandomOrderCollection(@PathVariable Long id) {
        CollectionEntity collection = collectionService.findById(id);
        CollectionDao collectionDao = collectionService.toDao(collection);
        return new ResponseEntity<>(collectionDao, HttpStatus.OK);
    }

    @GetMapping("/myCollections")
    ResponseEntity<List<List<CollectionDao>>> getPersonalCollections(@CurrentSecurityContext(expression = "authentication?.name")
                                                                          String username) {
        List<List<CollectionDao>> collections = collectionService.getPersonalCollectionData(username);
        return new ResponseEntity<>(collections, HttpStatus.OK);
    }

    @PostMapping("")
    ResponseEntity<String> createCollection(@Valid @RequestBody CollectionDto collectionDto,
                                                       @CurrentSecurityContext(expression = "authentication?.name")
                                                       String username) {
        String id = collectionService.createCollection(username, collectionDto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> deleteCollection(@PathVariable Long id,
                                                       @CurrentSecurityContext(expression = "authentication?.name")
                                                       String name) {
        collectionService.deleteIfAllowed(name, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/like")
    ResponseEntity<HttpStatus> upvoteCollection(@PathVariable Long id, @CurrentSecurityContext
            (expression = "authentication?.name") String username) {
        collectionService.upvoteCollection(id, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search/{title}")
    ResponseEntity<List<CollectionDao>> findCollectionsByTitle(@PathVariable String title) {
        return new ResponseEntity<>(collectionService.searchByTitle(title), HttpStatus.OK);
    }
}
