package com.flashcard.flashback.collection.data.mapper;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.data.mapper.CardMapper;
import com.flashcard.flashback.card.entity.CardEntity;
import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.data.CollectionDto;
import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.user.entity.UsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CollectionMapper {

    CollectionMapper INSTANCE = Mappers.getMapper(CollectionMapper.class);

    CollectionEntity dtoToEntity(CollectionDto collectionDto, UsersEntity user);

    @Mapping(source = "owners", target = "owners", qualifiedByName = "userEntityToId")
    @Mapping(source = "cards", target = "cards", qualifiedByName = "cardEntityToDao")
    CollectionDao entityToDao(CollectionEntity collection);

    @Named("cardEntityToDao")
    default CardDao cardEntityToDao(CardEntity card) {
        return CardMapper.INSTANCE.entityToDao(card);
    }

    @Named("userEntityToId")
    default Long userEntityToId(UsersEntity user) {
        return user.getId();
    }
}
