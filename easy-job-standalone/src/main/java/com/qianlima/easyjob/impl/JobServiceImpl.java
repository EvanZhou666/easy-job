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

package com.qianlima.easyjob.impl;

import com.qianlima.easyjob.entity.JobEntity;
import com.qianlima.easyjob.service.JobService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private Scheduler scheduler;

    /*Spring会处理该注解，从Spring容器中获取该bean*/
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void addJob(JobEntity job) {
        try {
            // 保存到数据库
            entityManager.persist(job);
            /* job状态是关闭，则置为停止状态*/
            if (job.getStatus()) {
                startJob(job);
            }
//            scheduler.pauseJob(JobKey.jobKey(job.getJobName(), job.getJobGroup()));
//            job.setStatus(false);
//            entityManager.merge(job);
        } catch (Exception e) {
            log.error("Add job failed", e);
            throw new RuntimeException("Add job failed", e);
        }
    }

    public void startJob(JobEntity job) throws ClassNotFoundException, SchedulerException {
        // 创建JobDetail
        Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(job.getJobClass());
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(job.getJobName(), job.getJobGroup())
                .withDescription(job.getDescription())
                .build();

        // 创建Trigger
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(job.getJobName() + "_trigger", job.getJobGroup())
                .withSchedule(cronScheduleBuilder)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Override
    @Transactional
    public void updateJob(JobEntity job) {
        try {
            JobEntity existingJob = entityManager.find(JobEntity.class, job.getId());
            if (existingJob == null) {
                throw new RuntimeException("Job not found");
            }

            // 更新数据库
            existingJob.setCronExpression(job.getCronExpression());
            existingJob.setDescription(job.getDescription());
            existingJob.setStatus(job.getStatus());
            existingJob.setParams(job.getParams());
            
            // 更新调度器
            TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName() + "_trigger", job.getJobGroup());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            trigger = trigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withSchedule(cronScheduleBuilder)
                    .build();
            
            scheduler.rescheduleJob(triggerKey, trigger);
            
            if (!job.getStatus()) {
                scheduler.pauseJob(JobKey.jobKey(job.getJobName(), job.getJobGroup()));
            } else {
                scheduler.resumeJob(JobKey.jobKey(job.getJobName(), job.getJobGroup()));
            }
        } catch (Exception e) {
            log.error("Update job failed", e);
            throw new RuntimeException("Update job failed", e);
        }
    }

    @Override
    @Transactional
    public void deleteJob(Long jobId) {
        try {
            JobEntity job = entityManager.find(JobEntity.class, jobId);
            if (job != null) {
                scheduler.deleteJob(JobKey.jobKey(job.getJobName(), job.getJobGroup()));
                entityManager.remove(job);
            }
        } catch (Exception e) {
            log.error("Delete job failed", e);
            throw new RuntimeException("Delete job failed", e);
        }
    }

    @Override
    @Transactional
    public void pauseJob(Long jobId) {
        try {
            JobEntity job = entityManager.find(JobEntity.class, jobId);
            if (job != null) {
                scheduler.pauseJob(JobKey.jobKey(job.getJobName(), job.getJobGroup()));
                job.setStatus(false);
                entityManager.merge(job);
            }
        } catch (Exception e) {
            log.error("Pause job failed", e);
            throw new RuntimeException("Pause job failed", e);
        }
    }

    @Override
    @Transactional
    public void resumeJob(Long jobId) {
        try {
            JobEntity job = entityManager.find(JobEntity.class, jobId);
            if (job != null) {
                scheduler.resumeJob(JobKey.jobKey(job.getJobName(), job.getJobGroup()));
                job.setStatus(true);
                entityManager.merge(job);
            }
        } catch (Exception e) {
            log.error("Resume job failed", e);
            throw new RuntimeException("Resume job failed", e);
        }
    }

    @Override
    public void runJobNow(Long jobId) {
        try {
            JobEntity job = entityManager.find(JobEntity.class, jobId);
            if (job != null) {
                scheduler.triggerJob(JobKey.jobKey(job.getJobName(), job.getJobGroup()));
            }
        } catch (Exception e) {
            log.error("Run job now failed", e);
            throw new RuntimeException("Run job now failed, you must start job first", e);
        }
    }

    @Override
    public List<JobEntity> listJobs() {
        return entityManager.createQuery("SELECT j FROM JobEntity j", JobEntity.class)
                .getResultList();
    }

    @Override
    public List<JobEntity> pageJobs(Integer page, Integer pageSize) {
        return entityManager.createQuery("SELECT j FROM JobEntity j", JobEntity.class)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public JobEntity getJobById(Long jobId) {
        return entityManager.find(JobEntity.class, jobId);
    }

    @Override
    public JobEntity getJobByName(String jobName, String jobGroup) {
        return entityManager.createQuery("SELECT j FROM JobEntity j WHERE j.jobName = :jobName AND j.jobGroup = :jobGroup",  JobEntity.class)
                .setParameter("jobName", jobName)
                .setParameter("jobGroup", jobGroup)
                .getSingleResult();
    }
}
