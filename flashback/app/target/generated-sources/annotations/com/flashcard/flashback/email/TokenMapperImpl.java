package com.flashcard.flashback.email;

import com.flashcard.flashback.user.UsersEntity;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-10T21:36:19+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
public class TokenMapperImpl implements TokenMapper {

    @Override
    public VerificationToken mapToken(String token, UsersEntity usersEntity) {
        if ( token == null && usersEntity == null ) {
            return null;
        }

        VerificationToken verificationToken = new VerificationToken();

        if ( usersEntity != null ) {
            verificationToken.setUsersEntity( usersEntity );
            verificationToken.setId( usersEntity.getId() );
        }
        verificationToken.setToken( token );

        return verificationToken;
    }
}
