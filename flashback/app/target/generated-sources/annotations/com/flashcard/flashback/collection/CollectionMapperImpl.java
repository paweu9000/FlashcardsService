package com.flashcard.flashback.collection;

import com.flashcard.flashback.card.CardEntity;
import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.collection.data.CollectionDao;
import com.flashcard.flashback.collection.data.CollectionDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-10T21:36:19+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
public class CollectionMapperImpl implements CollectionMapper {

    @Override
    public CollectionEntity dtoToEntity(CollectionDto collectionDto) {
        if ( collectionDto == null ) {
            return null;
        }

        CollectionEntity collectionEntity = new CollectionEntity();

        collectionEntity.setTitle( collectionDto.getTitle() );
        collectionEntity.setLikes( collectionDto.getLikes() );

        return collectionEntity;
    }

    @Override
    public CollectionDao entityToDao(CollectionEntity collection) {
        if ( collection == null ) {
            return null;
        }

        CollectionDao collectionDao = new CollectionDao();

        collectionDao.setOwners( userEntityToId( collection.getOwners() ) );
        collectionDao.setCards( cardEntityListToCardDaoList( collection.getCards() ) );
        collectionDao.setId( collection.getId() );
        collectionDao.setTitle( collection.getTitle() );
        collectionDao.setLikes( collection.getLikes() );

        return collectionDao;
    }

    protected List<CardDao> cardEntityListToCardDaoList(List<CardEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<CardDao> list1 = new ArrayList<CardDao>( list.size() );
        for ( CardEntity cardEntity : list ) {
            list1.add( cardEntityToDao( cardEntity ) );
        }

        return list1;
    }
}
