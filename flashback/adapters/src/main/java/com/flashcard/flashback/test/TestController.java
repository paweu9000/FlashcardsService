package com.flashcard.flashback.test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/test")
record TestController(TestService testService) {

    @GetMapping("/{collectionId}")
    ResponseEntity<TestEntity> getTest(@PathVariable Long collectionId) {
        return new ResponseEntity<>(testService.getTestEntityByCollectionId(collectionId), HttpStatus.OK);
    }
}
