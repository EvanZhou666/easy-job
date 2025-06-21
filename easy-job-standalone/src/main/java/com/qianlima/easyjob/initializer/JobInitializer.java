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