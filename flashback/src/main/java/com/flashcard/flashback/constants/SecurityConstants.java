package com.flashcard.flashback.constants;

public record SecurityConstants (){

    //SET TO ENV VARIABLES LATER
    public static final int TOKEN_EXPIRATION_TIME = 7200000;
    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String REGISTER_PATH = "/api/auth/register";
    public static final String SECRET_KEY = "env variable";
}
