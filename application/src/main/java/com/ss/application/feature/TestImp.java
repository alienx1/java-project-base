package com.ss.application.feature;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final JobLauncher jobLauncher;
    private final Job job;

    // Constructor injection to inject all dependencies
    public TestImp(TestRepository testRepo, TestMapper testMap, JobLauncher jobLauncher,
            @Qualifier("runDemoJob") Job job) {
        this.testRepo = testRepo;
        this.testMap = testMap;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Override
    public Mono<TestModel> testInsert(TestModel testModel) {
        return testRepo.save(testMap.INSTANCE.testModelToTest(testModel)).map(x -> testMap.INSTANCE.testToTestModel(x));
    }

    @Override
    public Flux<TestModel> testSelect() {
        return testRepo.findAll().map(x -> testMap.INSTANCE.testToTestModel(x));
    }

    @Override
    public void testDemoJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(job, jobParameters);
            log.info("✅ Demo Job Started: {}", execution.getStatus());
        } catch (Exception e) {
            log.error("❌ Error running Demo Job", e);
        }
    }
}
