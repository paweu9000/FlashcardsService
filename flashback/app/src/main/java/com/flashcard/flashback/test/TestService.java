package com.flashcard.flashback.test;

import com.flashcard.flashback.card.CardEntity;
import com.flashcard.flashback.collection.CollectionEntity;
import com.flashcard.flashback.collection.CollectionService;
import com.flashcard.flashback.exception.InsufficientQuestionsException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
class TestService {
    final TestRepository repository;
    final CollectionService collectionService;

    public TestService(TestRepository repository, CollectionService collectionService) {
        this.repository = repository;
        this.collectionService = collectionService;
    }

    TestEntity getTestEntityByCollectionId(Long collectionId) {
        Optional<TestEntity> testEntity = repository.findByCollectionId(collectionId);
        return testEntity.orElseGet(() -> createTestEntity(collectionId));
    }

    TestEntity createTestEntity(Long collectionId) {
        CollectionEntity collection = collectionService.findById(collectionId);
        if (collection.getSize() < 4) throw new InsufficientQuestionsException(collectionId);
        TestEntity test = new TestEntity();
        test.setCollectionId(collectionId);
        generateQuestions(test, collection);
        return repository.save(test);
    }

    private void deleteTest(Long testId) {
        repository.deleteById(testId);
    }

    void generateQuestions(TestEntity test, CollectionEntity collection) {
        repository.save(test);
        String[] cardQuestions = new String[collection.getSize()];
        String[] cardAnswers = new String[collection.getSize()];
        List<CardEntity> cards = collection.getCards().stream().toList();
        for (int i = 0; i < cardQuestions.length; i++) {
            CardEntity card = cards.get(i);
            cardQuestions[i] = card.getSide();
            cardAnswers[i] = card.getValue();
        }
        Random random = new Random();
        Set<QuestionEntity> questionEntities = new HashSet<>();
        for (int i = 0; i < cardQuestions.length; i++) {
            QuestionEntity question = new QuestionEntity();
            question.setQuestion(cardQuestions[i]);
            question.setAnswer(cardAnswers[i]);
            int index = random.nextInt(4);
            int indexForLoop = 0;
            while(question.getAnswers().size() < 4) {
                if(indexForLoop == index) {
                    question.addAnswer(question.getAnswer());
                    indexForLoop++;
                }
                else {
                    int randomAnswerIndex = random.nextInt(cardAnswers.length);
                    if (randomAnswerIndex != index) {
                        question.addAnswer(cardAnswers[randomAnswerIndex]);
                        indexForLoop++;
                    }
                }
            }
            question.setTest(test);
            questionEntities.add(question);
        }
        test.setQuestions(questionEntities);
    }
}
