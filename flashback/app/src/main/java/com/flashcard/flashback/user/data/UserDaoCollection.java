package com.flashcard.flashback.user.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDaoCollection {
    private long id;
    private String title;
}
