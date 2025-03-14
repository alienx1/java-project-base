package com.ss.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.application.feature.Test;
import com.ss.domain.model.TestModel;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/public")
public class TestController {
    @Autowired
    private Test testFeature;

    @GetMapping
    public Flux<TestModel> getTest() {
        return testFeature.testSelect();
    }

    @PostMapping
    public Mono<TestModel> postMethodName(@RequestBody TestModel test) {
        return testFeature.testInsert(test);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name") String name) {

        if ("error".equals(name)) {
            throw new IllegalArgumentException("Invalid name parameter!");
        }
        return String.format("Hello %s!", name);
    }

}
