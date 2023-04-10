package com.flashcard.flashback.collection;

import com.flashcard.flashback.card.CardEntity;
import com.flashcard.flashback.card.CardMapper;
import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.data.CollectionDto;
import com.flashcard.flashback.user.UsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CollectionMapper {

    CollectionMapper INSTANCE = Mappers.getMapper(CollectionMapper.class);

    CollectionEntity dtoToEntity(CollectionDto collectionDto);

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
