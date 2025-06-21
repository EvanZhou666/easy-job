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
    public List<JobLogEntity> getJobLogs(Long jobId) {
        return entityManager.createQuery(
                "SELECT l FROM JobLogEntity l WHERE l.job.id = :jobId ORDER BY l.startTime DESC",
                JobLogEntity.class)
                .setParameter("jobId", jobId)
                .getResultList();
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
