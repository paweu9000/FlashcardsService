package com.flashcard.flashback.card.data.mapper;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.user.entity.UsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CardMapper {

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

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
