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

package site.qianlima.easyjob.controller;

import site.qianlima.easyjob.entity.JobEntity;
import site.qianlima.easyjob.entity.JobLogEntity;
import site.qianlima.easyjob.service.JobLogService;
import site.qianlima.easyjob.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for job management
 */
@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobLogService jobLogService;

    /**
     * Add a new job
     * @param job Job entity
     * @return Response entity
     */
    @PostMapping
    public ResponseEntity<Void> addJob(@RequestBody JobEntity job) {
        jobService.addJob(job);
        return ResponseEntity.ok().build();
    }

    /**
     * Update an existing job
     * @param id Job ID
     * @param job Job entity
     * @return Response entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateJob(@PathVariable Long id, @RequestBody JobEntity job) {
        job.setId(id);
        jobService.updateJob(job);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a job
     * @param id Job ID
     * @return Response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Pause a job
     * @param id Job ID
     * @return Response entity
     */
    @PostMapping("/{id}/pause")
    public ResponseEntity<Void> pauseJob(@PathVariable Long id) {
        jobService.pauseJob(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Resume a job
     * @param id Job ID
     * @return Response entity
     */
    @PostMapping("/{id}/resume")
    public ResponseEntity<Void> resumeJob(@PathVariable Long id) {
        jobService.resumeJob(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Execute a job immediately
     * @param id Job ID
     * @return Response entity
     */
    @PostMapping("/{id}/run")
    public ResponseEntity<Void> runJob(@PathVariable Long id) {
        jobService.runJobNow(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Get all jobs
     * @return List of all jobs
     */
    @GetMapping
    public ResponseEntity<List<JobEntity>> listJobs() {
        return ResponseEntity.ok(jobService.listJobs());
    }

    /**
     * Get job by ID
     * @param id Job ID
     * @return Job entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobEntity> getJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    /**
     * Get job logs
     * @param id Job ID
     * @return List of job logs
     */
    @GetMapping("/{id}/logs")
    public ResponseEntity<Map<String, Object>> getJobLogs(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        List<JobLogEntity> logs = jobLogService.getJobLogs(id, pageNum, pageSize);
        long total = jobLogService.getJobLogsCount(id);

        Map<String, Object> response = new HashMap<>();
        response.put("logs", logs);
        response.put("total", total);
        response.put("pageNum", pageNum);
        response.put("pageSize", pageSize);

        return ResponseEntity.ok(response);
    }

    /**
     * Clear job logs
     * @param id Job ID
     * @return Response entity
     */
    @DeleteMapping("/{id}/logs")
    public ResponseEntity<Void> clearJobLogs(@PathVariable Long id) {
        jobLogService.clearJobLogs(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Get job log by ID
     * @param logId Log ID
     * @return Job log entity
     */
    @GetMapping("/logs/{logId}")
    public ResponseEntity<JobLogEntity> getJobLog(@PathVariable Long logId) {
        return ResponseEntity.ok(jobLogService.getLogById(logId));
    }
}
