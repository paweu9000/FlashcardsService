package com.flashcard.flashback.test.data;

import lombok.Data;

import java.util.List;

@Data
public class TestDao {
    private Long Id;
    private List<QuestionDao> questions;
    private Long collectionId;
}
