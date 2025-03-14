package com.ss.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ss.domain.entity.db.Test;
import com.ss.domain.model.TestModel;

@Mapper(componentModel = "spring")
public interface TestMapper {
    TestMapper INSTANCE = Mappers.getMapper(TestMapper.class);

    TestModel testToTestModel(Test test);

    Test testModelToTest(TestModel test);

}
