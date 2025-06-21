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
