package com.flashcard.flashback.verification.mapper;

import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.verification.entity.VerificationToken;
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
