package com.flashcard.flashback.test;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
interface TestMapper {

    TestMapper INSTANCE = Mappers.getMapper(TestMapper.class);
}
