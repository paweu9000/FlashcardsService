package com.flashcard.flashback.test.data;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDao {
    private String question;
    private List<String> answers;
    private String answer;
}
