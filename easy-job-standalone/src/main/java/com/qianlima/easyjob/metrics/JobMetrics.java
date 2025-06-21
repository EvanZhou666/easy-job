/*
 * MIT License
 *
 * Copyright (c) 2025 EvanZhou666
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
