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

import com.qianlima.easyjob.entity.JobLogEntity;
import com.qianlima.easyjob.service.JobLogService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobLogServiceImpl implements JobLogService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(JobLogEntity jobLog) {
        entityManager.persist(jobLog);
    }

    @Override
    public List<JobLogEntity> getJobLogs(Long jobId, int pageNum, int pageSize) {
        return entityManager.createQuery(
                "SELECT l FROM JobLogEntity l WHERE l.job.id = :jobId ORDER BY l.startTime DESC",
                JobLogEntity.class)
                .setParameter("jobId", jobId)
                .setFirstResult((pageNum - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
    @Override
    public long getJobLogsCount(Long jobId) {
        return entityManager.createQuery(
                "SELECT COUNT(l) FROM JobLogEntity l WHERE l.job.id = :jobId",
                Long.class)
                .setParameter("jobId", jobId)
                .getSingleResult();
    }

    public List<JobLogEntity> getJobLogs(Long jobId) {
        return getJobLogs(jobId, 1, Integer.MAX_VALUE);
    }

    @Override
    @Transactional
    public void clearJobLogs(Long jobId) {
        entityManager.createQuery("DELETE FROM JobLogEntity l WHERE l.job.id = :jobId")
                .setParameter("jobId", jobId)
                .executeUpdate();
    }

    @Override
    public JobLogEntity getLogById(Long logId) {
        return entityManager.find(JobLogEntity.class, logId);
    }
}
