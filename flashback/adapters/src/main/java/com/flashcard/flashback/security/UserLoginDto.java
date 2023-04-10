package com.flashcard.flashback.security;

import lombok.Data;

@Data
public class UserLoginDto {
    private String emailOrLogin;
    private String password;
}
