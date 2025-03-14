package com.ss.application.feature;

import com.ss.domain.model.TestModel;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Test  {
    public Mono<TestModel> testInsert(TestModel testModel);
    
    public Flux<TestModel> testSelect();
}
