package com.qianlima.easyjob.job;

import com.qianlima.easyjob.entity.JobLogEntity;
import com.qianlima.easyjob.metrics.JobMetrics;
import com.qianlima.easyjob.notification.NotificationService;
import com.qianlima.easyjob.service.JobLogService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class BaseJob implements Job {
    @Autowired
    private JobLogService jobLogService;

    @Autowired
    private JobMetrics jobMetrics;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void execute(JobExecutionContext context) {
        long startTime = System.currentTimeMillis();
        JobLogEntity jobLog = new JobLogEntity();
        jobLog.setJobName(context.getJobDetail().getKey().getName());
        jobLog.setJobGroup(context.getJobDetail().getKey().getGroup());
        jobLog.setJobClass(this.getClass().getName());
        
        var timerSample = jobMetrics.startTimer();
        try {
            log.info("Job {} started", context.getJobDetail().getKey());
            doExecute(context);
            jobLog.setStatus("SUCCESS");
            jobLog.setMessage("successfully");
            jobMetrics.recordJobExecution();
        } catch (Exception e) {
            log.error("Job {} failed", context.getJobDetail().getKey(), e);
            jobLog.setStatus("FAILED");
            jobLog.setExceptionInfo(e.getMessage());
            jobMetrics.recordJobFailure();
            notificationService.sendNotification(
                "Job Execution Failed: " + context.getJobDetail().getKey(),
                "Job failed with error: " + e.getMessage());
        } finally {
            jobLog.setExecutionTime(System.currentTimeMillis() - startTime);
            jobLogService.save(jobLog);
            jobMetrics.stopTimer(timerSample);
        }
    }

    /**
     * 具体的任务执行逻辑
     */
    protected abstract void doExecute(JobExecutionContext context) throws Exception;
}
