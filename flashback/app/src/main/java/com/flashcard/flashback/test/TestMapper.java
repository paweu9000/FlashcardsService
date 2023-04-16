package com.flashcard.flashback.test;

import com.flashcard.flashback.test.data.QuestionDao;
import com.flashcard.flashback.test.data.TestDao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
interface TestMapper {

    TestMapper INSTANCE = Mappers.getMapper(TestMapper.class);

    @Mapping(source = "questions", target = "questions", qualifiedByName = "questionEntitiesToDao")
    TestDao toDao(TestEntity test);

    @Named("questionEntitiesToDao")
    default List<QuestionDao> questionEntitiesToDao(Set<QuestionEntity> questions) {
        return questions.stream().map(this::questionEntityToDao).toList();
    }

    @Named("questionEntityToDao")
    default QuestionDao questionEntityToDao(QuestionEntity question) {
        return QuestionDao.builder()
                .question(question.getQuestion())
                .answer(question.getAnswer())
                .answers(question.getAnswers().stream().toList())
                .build();
    }
}
