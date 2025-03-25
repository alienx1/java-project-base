package com.ss.infrastructure.jobs.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DemoScheduler {

    private final JobLauncher jobLauncher;
    private final Job job;

    public DemoScheduler(JobLauncher jobLauncher, @Qualifier("runDemoJob") Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void runJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(job, jobParameters);
            log.info("✅ Demo Job Scheduler Started: {}", execution.getStatus());
        } catch (Exception e) {
            log.error("❌ Error running Demo Job", e);
        }
    }
}
