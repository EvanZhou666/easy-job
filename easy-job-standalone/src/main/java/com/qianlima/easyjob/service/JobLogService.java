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

import com.qianlima.easyjob.entity.JobLogEntity;

import java.util.List;

/**
 * Service interface for job log management
 */
public interface JobLogService {
    /**
     * Save job execution log
     * @param jobLog Job log entity
     */
    void save(JobLogEntity jobLog);

    /**
     * Get job logs by job ID
     * @param jobId Job ID
     * @return List of job logs
     */
/**
     * Get job logs by job ID with pagination
     * @param jobId Job ID
     * @param pageNum Page number (starting from 1)
     * @param pageSize Page size
     * @return List of job logs for the specified page
     */
    List<JobLogEntity> getJobLogs(Long jobId, int pageNum, int pageSize);

    /**
     * Get total count of job logs by job ID
     * @param jobId Job ID
     * @return Total count of job logs
     */
    long getJobLogsCount(Long jobId);

    /**
     * Clear logs of the specified job
     * @param jobId Job ID
     */
    void clearJobLogs(Long jobId);

    /**
     * Get job log details by log ID
     * @param logId Log ID
     * @return Job log entity
     */
    JobLogEntity getLogById(Long logId);
}
