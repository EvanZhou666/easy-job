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

package site.qianlima.easyjob.entity;

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
