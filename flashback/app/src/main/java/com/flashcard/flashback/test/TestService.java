package com.flashcard.flashback.test;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    final TestRepository repository;

    public TestService(TestRepository repository) {
        this.repository = repository;
    }
}
