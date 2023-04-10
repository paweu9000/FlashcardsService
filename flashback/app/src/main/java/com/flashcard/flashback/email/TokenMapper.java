package com.flashcard.flashback.email;

import com.flashcard.flashback.user.UsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TokenMapper {

    TokenMapper INSTANCE = Mappers.getMapper(TokenMapper.class);

    @Mapping(target = "token", source = "token")
    @Mapping(target = "usersEntity", source = "usersEntity")
    VerificationToken mapToken(String token, UsersEntity usersEntity);
}
