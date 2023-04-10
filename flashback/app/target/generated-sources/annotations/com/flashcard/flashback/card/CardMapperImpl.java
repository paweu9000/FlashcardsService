package com.flashcard.flashback.card;

import com.flashcard.flashback.card.data.CardDao;
import com.flashcard.flashback.card.data.CardDto;
import com.flashcard.flashback.collection.CollectionService;
import com.flashcard.flashback.user.UserService;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-10T21:36:20+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class CardMapperImpl implements CardMapper {

    @Override
    public CardEntity toCardEntity(CardDto cardDto, UserService userService, CollectionService collectionService) {
        if ( cardDto == null ) {
            return null;
        }

        CardEntity cardEntity = new CardEntity();

        cardEntity.setSide( cardDto.getSide() );
        cardEntity.setValue( cardDto.getValue() );

        cardEntity.setCreatedBy( userService.findById(cardDto.getCreatorId()) );
        cardEntity.setCollector( collectionService.findById(cardDto.getCollectionId()) );

        return cardEntity;
    }

    @Override
    public CardDao entityToDao(CardEntity card) {
        if ( card == null ) {
            return null;
        }

        CardDao cardDao = new CardDao();

        cardDao.setCollector( collectionEntityToId( card.getCollector() ) );
        cardDao.setCreatedBy( userEntityToId( card.getCreatedBy() ) );
        cardDao.setId( card.getId() );
        cardDao.setSide( card.getSide() );
        cardDao.setValue( card.getValue() );

        return cardDao;
    }
}
