package com.flashcard.flashback.security;

record SecurityConstants (){

    //SET TO ENV VARIABLES LATER
    static final int TOKEN_EXPIRATION_TIME = 7200000;
    static final String BEARER = "Bearer ";
    static final String AUTHORIZATION = "Authorization";
    static final String REGISTER_PATH = "/api/auth/register";
    static final String CARDS_PATH = "/api/cards/*";
    static final String COLLECTION_PATH = "/api/collection/*";
    static final String USER_PATH = "/api/user/*";
    static final String DOCUMENTATION_PATH = "/api/documentation/*";
    static final String SECRET_KEY = System.getenv("SECRET_KEY");
    static final String VERIFY_PATH = "/api/verify";
    static final String TEST_PATH = "/api/test";
}
