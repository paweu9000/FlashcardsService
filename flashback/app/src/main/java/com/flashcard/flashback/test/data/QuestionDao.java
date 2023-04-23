package com.flashcard.flashback.test.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionDao {
    private Long id;
    private String question;
    private List<String> answers;
    private String answer;
}
