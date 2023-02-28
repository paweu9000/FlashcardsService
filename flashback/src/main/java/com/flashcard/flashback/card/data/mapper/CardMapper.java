package com.flashcard.flashback.card.data.mapper;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.data.CardDto;
import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.collection.service.CollectionService;
import com.flashcard.flashback.user.entity.UsersEntity;
import com.flashcard.flashback.user.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface CardMapper {

    @Autowired
    UserService userService = null;
    @Autowired
    CollectionService collectionService = null;

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    @Mapping(target = "side", source = "side")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "createdBy", expression = "java(userService.findById(cardDto.getCreatorId()))")
    @Mapping(target = "collector", expression = "java(collectionService.findById(cardDto.getCollectionId()))")
    CardEntity toCardEntity(CardDto cardDto);

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
