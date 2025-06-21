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
