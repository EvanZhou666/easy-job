package com.qianlima.easyjob.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Job log entity for storing job execution logs
 */
@Data
@Entity
@Table(name = "job_log_entity")
public class JobLogEntity {
    /**
     * Log ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    /**
     * Job name
     */
    @Column(nullable = false)
    private String jobName;

    /**
     * Job group
     */
    @Column(nullable = false)
    private String jobGroup;

    /**
     * Job class name
     */
    @Column(nullable = false)
    private String jobClass;

    /**
     * Job data used for this execution
     */
    private String params;

    /**
     * Execution start time
     */
    @CreationTimestamp
    private LocalDateTime startTime;

    /**
     * Execution end time
     */
    private LocalDateTime endTime;

    /**
     * Execution status: 0-failed, 1-success
     */
    private String status;

    /**
     * Execution result or error message
     */
    @Column(length = 4000)
    private String message;

    /**
     * Exception stack trace (if any)
     */
    private String exceptionInfo;

    /**
     * Execution time in milliseconds
     */
    private Long executionTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private JobEntity job;
}
