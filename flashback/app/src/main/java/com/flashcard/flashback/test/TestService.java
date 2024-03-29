package com.flashcard.flashback.test;

import com.flashcard.flashback.card.CardEntity;
import com.flashcard.flashback.collection.CollectionEntity;
import com.flashcard.flashback.collection.CollectionObserver;
import com.flashcard.flashback.collection.CollectionService;
import com.flashcard.flashback.exception.EntityNotFoundException;
import com.flashcard.flashback.exception.InsufficientQuestionsException;
import com.flashcard.flashback.test.data.QuestionDao;
import com.flashcard.flashback.test.data.TestDao;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
class TestService implements ApplicationListener<CollectionObserver> {
    final TestRepository repository;
    CollectionService collectionService;

    public TestService(TestRepository repository, CollectionService collectionService) {
        this.repository = repository;
        this.collectionService = collectionService;
    }

    void setCollectionService(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    TestEntity getTestEntityByCollectionId(Long collectionId) {
        Optional<TestEntity> testEntity = repository.findByCollectionId(collectionId);
        return testEntity.orElseGet(() -> createTestEntity(collectionId));
    }

    TestDao toDao(Long collectionId) {
        return TestMapper.INSTANCE.toDao(getTestEntityByCollectionId(collectionId));
    }

    TestEntity createTestEntity(Long collectionId) {
        CollectionEntity collection = collectionService.findById(collectionId);
        if (collection.getSize() < 4) throw new InsufficientQuestionsException(collectionId);
        TestEntity test = new TestEntity();
        test.setCollectionId(collectionId);
        generateQuestions(test, collection);
        return test;
    }

    void deleteTestById(Long id) {
        repository.deleteById(id);
    }

    void deleteTest(Long collectionId) {
        repository.findByCollectionId(collectionId).ifPresent(test -> deleteTestById(test.getId()));
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
            question.addAnswer(question.getAnswer());
            while(question.getAnswers().size() < 4) {
                int randomAnswerIndex = random.nextInt(cardAnswers.length);
                if (randomAnswerIndex != i) {
                    question.addAnswer(cardAnswers[randomAnswerIndex]);
                }
            }
            question.setTest(test);
            questionEntities.add(question);
        }
        test.setQuestions(questionEntities);
        repository.save(test);
    }

    TestEntity unwrapTest(Optional<TestEntity> test) {
        return test.orElseThrow(() -> new EntityNotFoundException(TestEntity.class));
    }

    TestDao toSortedDao(Long collectionId) {
        TestDao test = toDao(collectionId);

        test.setQuestions(test.getQuestions()
                .stream().sorted(Comparator.comparingLong(QuestionDao::getId)).toList());

        return test;
    }

    @Override
    public void onApplicationEvent(CollectionObserver event) {
        deleteTest(event.getCollectionId());
    }
}
