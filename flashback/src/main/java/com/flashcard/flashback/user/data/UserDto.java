package com.flashcard.flashback.user.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String login;
    private String username;
    private String email;
    private String password;
}
