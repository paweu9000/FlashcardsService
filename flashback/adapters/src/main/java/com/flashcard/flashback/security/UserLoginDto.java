package com.flashcard.flashback.security;

import lombok.Data;

@Data
class UserLoginDto {
    private String emailOrLogin;
    private String password;
}
