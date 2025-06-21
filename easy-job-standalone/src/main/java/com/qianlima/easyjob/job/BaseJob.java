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

package com.qianlima.easyjob.job;

import com.qianlima.easyjob.entity.JobEntity;
import com.qianlima.easyjob.entity.JobLogEntity;
import com.qianlima.easyjob.metrics.JobMetrics;
import com.qianlima.easyjob.notification.NotificationService;
import com.qianlima.easyjob.service.JobLogService;
import com.qianlima.easyjob.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
public abstract class BaseJob implements Job {
    @Autowired
    private JobLogService jobLogService;

    @Autowired
    private JobMetrics jobMetrics;

    @Autowired
    private JobService jobService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void execute(JobExecutionContext context) {
        long startTime = System.currentTimeMillis();
        JobLogEntity jobLog = new JobLogEntity();
        jobLog.setJobName(context.getJobDetail().getKey().getName());
        jobLog.setJobGroup(context.getJobDetail().getKey().getGroup());
        jobLog.setJobClass(this.getClass().getName());
        JobEntity jobEntity = jobService.getJobByName(jobLog.getJobName(), jobLog.getJobGroup());
        if (jobEntity != null) {
            jobLog.setJob(jobEntity);
        }
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
            jobLog.setMessage(e.getClass().getName());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            jobLog.setExceptionInfo(sw.toString());
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
