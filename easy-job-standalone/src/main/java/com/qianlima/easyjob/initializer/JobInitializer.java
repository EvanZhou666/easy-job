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

package com.qianlima.easyjob.initializer;
import com.qianlima.easyjob.entity.JobEntity;
import com.qianlima.easyjob.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.List;
@Slf4j
@Component
public class JobInitializer {
    @Autowired
    private JobService jobService;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeJobs() {
        try {
            log.info("Starting to initialize jobs from database...");
            List<JobEntity> jobs = jobService.listJobs();

            for (JobEntity job : jobs) {
                try {
                    if (job.getStatus()) {
                        jobService.startJob(job);
                    }
                    log.info("Successfully initialized job: {}.{}", job.getJobGroup(), job.getJobName());
                } catch (Exception e) {
                    log.error("Failed to initialize job: {}.{}", job.getJobGroup(), job.getJobName(), e);
                }
            }
            log.info("Completed initializing {} jobs from database", jobs.size());
        } catch (Exception e) {
            log.error("Failed to initialize jobs", e);
        }
    }

}