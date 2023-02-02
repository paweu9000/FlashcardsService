package com.flashcard.flashback.user.data;

import lombok.Data;

@Data
public class UserDto {
    private String login;
    private String username;
    private String email;
    private String password;
}
