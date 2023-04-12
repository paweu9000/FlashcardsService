package com.flashcard.flashback.user;

import com.flashcard.flashback.collection.CollectionEntity;
import com.flashcard.flashback.user.data.UserDao;
import com.flashcard.flashback.user.data.UserDaoCollection;
import com.flashcard.flashback.user.data.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "collections", source = "collections", qualifiedByName = "collectionEntitiesToUserDaoCollection")
    @Mapping(target = "savedCollections", source = "savedCollections", qualifiedByName = "collectionEntitiesToUserDaoCollection")
    UserDao entityToDao(UsersEntity entity);

    @Named("collectionEntityToUserCollectionDao")
    default UserDaoCollection collectionEntityToUserDaoCollection(CollectionEntity entity) {
        return UserDaoCollection.builder().id(entity.getId()).title(entity.getTitle()).build();
    }

    @Named("collectionEntitiesToUserDaoCollection")
    default List<UserDaoCollection> collectionEntitiesToUserDaoCollection(Set<CollectionEntity> entities) {
        return entities.stream().map(this::collectionEntityToUserDaoCollection).toList();
    }

    UsersEntity dtoToEntity(UserDto userDto);
    UserDto entityToDto(UsersEntity user);
}
