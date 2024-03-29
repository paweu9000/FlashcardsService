package com.flashcard.flashback.card;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.data.CardDto;
import com.flashcard.flashback.collection.CollectionEntity;
import com.flashcard.flashback.collection.CollectionService;
import com.flashcard.flashback.user.UserService;
import com.flashcard.flashback.user.UsersEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface CardMapper {
    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    @Mapping(target = "side", source = "side")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "createdBy", expression = "java(userService.findById(cardDto.getCreatorId()))")
    @Mapping(target = "collector", expression = "java(collectionService.findById(cardDto.getCollectionId()))")
    CardEntity toCardEntity(CardDto cardDto, @Context UserService userService, @Context CollectionService collectionService);

    @Mapping(target = "collector", source = "collector", qualifiedByName = "collectionEntityToId")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userEntityToId")
    CardDao entityToDao(CardEntity card);

    @Named("collectionEntityToId")
    default Long collectionEntityToId(CollectionEntity collection) {
        return collection.getId();
    }

    @Named("userEntityToId")
    default Long userEntityToId(UsersEntity user) {
        return user.getId();
    }
}
