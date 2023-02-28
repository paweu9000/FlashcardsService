package com.flashcard.flashback.user.data.mapper;

import com.flashcard.flashback.collection.entity.CollectionEntity;
import com.flashcard.flashback.user.data.UserDao;
import com.flashcard.flashback.user.entity.UsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "collections", source = "collections", qualifiedByName = "collectionEntityToId")
    @Mapping(target = "savedCollections", source = "savedCollections", qualifiedByName = "collectionEntityToId")
    UserDao entityToDao(UsersEntity entity);

    @Named("collectionEntityToId")
    default Long collectionEntityToId(CollectionEntity entity) {
        return entity.getId();
    }
}
