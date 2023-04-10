package com.flashcard.flashback.user;

import com.flashcard.flashback.collection.CollectionEntity;
import com.flashcard.flashback.user.data.UserDao;
import com.flashcard.flashback.user.data.UserDaoCollection;
import com.flashcard.flashback.user.data.UserDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-10T21:36:19+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDao entityToDao(UsersEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UserDao userDao = new UserDao();

        userDao.setCollections( collectionEntityListToUserDaoCollectionList( entity.getCollections() ) );
        userDao.setSavedCollections( collectionEntityListToLongList( entity.getSavedCollections() ) );
        userDao.setId( entity.getId() );
        userDao.setUsername( entity.getUsername() );

        return userDao;
    }

    @Override
    public UsersEntity dtoToEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setLogin( userDto.getLogin() );
        usersEntity.setUsername( userDto.getUsername() );
        usersEntity.setEmail( userDto.getEmail() );
        usersEntity.setPassword( userDto.getPassword() );

        return usersEntity;
    }

    @Override
    public UserDto entityToDto(UsersEntity user) {
        if ( user == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.login( user.getLogin() );
        userDto.username( user.getUsername() );
        userDto.email( user.getEmail() );
        userDto.password( user.getPassword() );

        return userDto.build();
    }

    protected List<UserDaoCollection> collectionEntityListToUserDaoCollectionList(List<CollectionEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<UserDaoCollection> list1 = new ArrayList<UserDaoCollection>( list.size() );
        for ( CollectionEntity collectionEntity : list ) {
            list1.add( collectionEntityToUserDaoCollection( collectionEntity ) );
        }

        return list1;
    }

    protected List<Long> collectionEntityListToLongList(List<CollectionEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<Long> list1 = new ArrayList<Long>( list.size() );
        for ( CollectionEntity collectionEntity : list ) {
            list1.add( collectionEntityToId( collectionEntity ) );
        }

        return list1;
    }
}
