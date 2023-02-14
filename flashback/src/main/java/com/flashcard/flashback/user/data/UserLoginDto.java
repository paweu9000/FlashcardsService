package com.flashcard.flashback.user.data;

import lombok.Data;

@Data
public class UserLoginDto {
    private String loginOrUsername;
    private String password;
}
