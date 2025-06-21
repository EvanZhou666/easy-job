package com.qianlima.easyjob.service;

import com.qianlima.easyjob.entity.JobEntity;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * Service interface for job management
 */
public interface JobService {
    
    /**
     * Add a new job
     * @param jobEntity Job entity
     */
    void addJob(JobEntity jobEntity);
    
    /**
     * Update an existing job
     * @param jobEntity Job entity
     */
    void updateJob(JobEntity jobEntity);
    
    /**
     * Delete a job by ID
     * @param jobId Job ID
     */
    void deleteJob(Long jobId);
    
    /**
     * Pause a job
     * @param jobId Job ID
     */
    void pauseJob(Long jobId);
    
    /**
     * Resume a paused job
     * @param jobId Job ID
     */
    void resumeJob(Long jobId);
    
    /**
     * Execute a job immediately
     * @param jobId Job ID
     */
    void runJobNow(Long jobId);
    
    /**
     * Get all jobs
     * @return List of all jobs
     */
    List<JobEntity> listJobs();

    List<JobEntity> pageJobs(Integer page, Integer size);


    /**
     * Get a job by ID
     * @param jobId Job ID
     * @return Job entity
     */
    JobEntity getJobById(Long jobId);

    /**
     * Get a job by name And GroupName
     * @param jobName
     * @param groupName
     * @return
     */
    JobEntity getJobByName(String jobName, String groupName);

    /**
     * tell quartz schedule start job
     * @param job
     * @throws ClassNotFoundException
     * @throws SchedulerException
     */
    void startJob(JobEntity job) throws ClassNotFoundException, SchedulerException;
}
