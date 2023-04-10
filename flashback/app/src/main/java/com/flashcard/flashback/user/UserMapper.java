package com.flashcard.flashback.user;

import com.flashcard.flashback.collection.CollectionEntity;
import com.flashcard.flashback.user.data.UserDao;
import com.flashcard.flashback.user.data.UserDaoCollection;
import com.flashcard.flashback.user.data.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "collections", source = "collections", qualifiedByName = "collectionEntityToUserDaoCollection")
    @Mapping(target = "savedCollections", source = "savedCollections", qualifiedByName = "collectionEntityToId")
    UserDao entityToDao(UsersEntity entity);

    @Named("collectionEntityToId")
    default Long collectionEntityToId(CollectionEntity entity) {
        return entity.getId();
    }

    @Named("collectionEntityToUserDaoCollection")
    default UserDaoCollection collectionEntityToUserDaoCollection(CollectionEntity entity) {
        UserDaoCollection collection = new UserDaoCollection();
        collection.setId(entity.getId());
        collection.setTitle(entity.getTitle());
        return collection;
    }

    UsersEntity dtoToEntity(UserDto userDto);
    UserDto entityToDto(UsersEntity user);
}
