package com.flashcard.flashback.constants;

public record SecurityConstants (){

    //SET TO ENV VARIABLES LATER
    public static final int TOKEN_EXPIRATION_TIME = 7200000;
    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String REGISTER_PATH = "/api/auth/register";
    public static final String CARDS_PATH = "/api/cards/*";
    public static final String COLLECTION_PATH = "/api/collection/*";
    public static final String USER_PATH = "/api/user/*";
    public static final String SECRET_KEY = System.getenv("SECRET_KEY");
    public static final String VERIFY_PATH = "/api/verify";
}
