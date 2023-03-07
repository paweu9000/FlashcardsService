package com.flashcard.flashback.user.data;

import lombok.Data;

import java.util.List;

@Data
public class UserDao {
    Long id;
    String username;
    List<Long> collections;
    List<Long> savedCollections;
}
