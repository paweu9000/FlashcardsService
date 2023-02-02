package com.flashcard.flashback.user.data;

import com.flashcard.flashback.user.entity.UsersEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDao {
    Long id;
    String username;
    List<Long> collections = new ArrayList<>();
    List<Long> savedCollections = new ArrayList<>();

    public UserDao(UsersEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        user.getCollections().forEach(collectionEntity -> collections.add(collectionEntity.getId()));
        user.getSavedCollections().forEach(collectionEntity -> collections.add(collectionEntity.getId()));
    }
}
