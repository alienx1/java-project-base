package com.ss.application.feature;

import org.springframework.stereotype.Service;

import com.ss.domain.mapper.TestMapper;
import com.ss.domain.model.TestModel;
import com.ss.persistence.repository.db.TestRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TestImp implements Test {
    private final TestRepository testRepo;
    private final TestMapper testMap;

    public TestImp(TestRepository testRepo, TestMapper testMap) {
        this.testRepo = testRepo;
        this.testMap = testMap;
    }

    @Override
    public Mono<TestModel> testInsert(TestModel testModel) {
        return testRepo.save(testMap.INSTANCE.testModelToTest(testModel)).map(x -> testMap.INSTANCE.testToTestModel(x));
    }

    @Override
    public Flux<TestModel> testSelect() {
        return testRepo.findAll().map(x -> testMap.INSTANCE.testToTestModel(x));
    }
}
