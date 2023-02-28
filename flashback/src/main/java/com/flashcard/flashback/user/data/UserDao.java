package com.flashcard.flashback.user.data;

import com.flashcard.flashback.user.entity.UsersEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDao {
    Long id;
    String username;
    List<Long> collections;
    List<Long> savedCollections;
}
