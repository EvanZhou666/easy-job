package com.qianlima.easyjob.statistics;

import com.qianlima.easyjob.entity.JobLogEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobStatisticsService {

    @PersistenceContext
    private EntityManager entityManager;

    public Map<String, Object> getJobStatistics(Long jobId, LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> statistics = new HashMap<>();
        
        // 获取总执行次数
        Long totalExecutions = entityManager.createQuery(
            "SELECT COUNT(l) FROM JobLogEntity l WHERE l.job.id = :jobId " +
            "AND l.startTime BETWEEN :startTime AND :endTime", Long.class)
            .setParameter("jobId", jobId)
            .setParameter("startTime", startTime)
            .setParameter("endTime", endTime)
            .getSingleResult();
        
        // 获取成功次数
        Long successCount = entityManager.createQuery(
            "SELECT COUNT(l) FROM JobLogEntity l WHERE l.job.id = :jobId " +
            "AND l.status = 'SUCCESS' AND l.startTime BETWEEN :startTime AND :endTime", Long.class)
            .setParameter("jobId", jobId)
            .setParameter("startTime", startTime)
            .setParameter("endTime", endTime)
            .getSingleResult();
        
        // 获取失败次数
        Long failureCount = entityManager.createQuery(
            "SELECT COUNT(l) FROM JobLogEntity l WHERE l.job.id = :jobId " +
            "AND l.status = 'FAILED' AND l.startTime BETWEEN :startTime AND :endTime", Long.class)
            .setParameter("jobId", jobId)
            .setParameter("startTime", startTime)
            .setParameter("endTime", endTime)
            .getSingleResult();
        
        // 获取平均执行时间
        Double avgExecutionTime = entityManager.createQuery(
            "SELECT AVG(l.executionTime) FROM JobLogEntity l WHERE l.job.id = :jobId " +
            "AND l.startTime BETWEEN :startTime AND :endTime", Double.class)
            .setParameter("jobId", jobId)
            .setParameter("startTime", startTime)
            .setParameter("endTime", endTime)
            .getSingleResult();
        
        // 获取最近的失败记录
        List<JobLogEntity> recentFailures = entityManager.createQuery(
            "SELECT l FROM JobLogEntity l WHERE l.job.id = :jobId " +
            "AND l.status = 'FAILED' AND l.startTime BETWEEN :startTime AND :endTime " +
            "ORDER BY l.startTime DESC", JobLogEntity.class)
            .setParameter("jobId", jobId)
            .setParameter("startTime", startTime)
            .setParameter("endTime", endTime)
            .setMaxResults(5)
            .getResultList();

        statistics.put("totalExecutions", totalExecutions);
        statistics.put("successCount", successCount);
        statistics.put("failureCount", failureCount);
        statistics.put("successRate", totalExecutions > 0 ? (double)successCount/totalExecutions * 100 : 0);
        statistics.put("avgExecutionTime", avgExecutionTime);
        statistics.put("recentFailures", recentFailures);
        
        return statistics;
    }
}
