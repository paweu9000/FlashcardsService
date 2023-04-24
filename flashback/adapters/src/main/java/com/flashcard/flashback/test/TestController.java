package com.flashcard.flashback.test;

import com.flashcard.flashback.test.data.TestDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
record TestController(TestService testService) {

    @GetMapping("/{collectionId}")
    ResponseEntity<TestDao> getSortedOrderTest(@PathVariable Long collectionId) {
        return new ResponseEntity<>(testService.toSortedDao(collectionId), HttpStatus.OK);
    }

    @GetMapping("/{collectionId}/random")
    ResponseEntity<TestDao> getRandomOrderTest(@PathVariable Long collectionId) {
        return new ResponseEntity<>(testService.toDao(collectionId), HttpStatus.OK);
    }
}
