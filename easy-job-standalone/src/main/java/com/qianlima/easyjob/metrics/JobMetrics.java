package com.qianlima.easyjob.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class JobMetrics {
    private final Counter jobExecutionCounter;
    private final Counter jobFailureCounter;
    private final Timer jobExecutionTimer;

    public JobMetrics(MeterRegistry registry) {
        this.jobExecutionCounter = Counter.builder("scheduler.job.executions")
                .description("Number of job executions")
                .register(registry);

        this.jobFailureCounter = Counter.builder("scheduler.job.failures")
                .description("Number of job failures")
                .register(registry);

        this.jobExecutionTimer = Timer.builder("scheduler.job.duration")
                .description("Job execution time")
                .register(registry);
    }

    public void recordJobExecution() {
        jobExecutionCounter.increment();
    }

    public void recordJobFailure() {
        jobFailureCounter.increment();
    }

    public Timer.Sample startTimer() {
        return Timer.start();
    }

    public void stopTimer(Timer.Sample sample) {
        sample.stop(jobExecutionTimer);
    }
}
