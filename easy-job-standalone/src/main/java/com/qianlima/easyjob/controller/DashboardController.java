package com.qianlima.easyjob.controller;

import com.qianlima.easyjob.entity.JobEntity;
import com.qianlima.easyjob.entity.JobLogEntity;
import com.qianlima.easyjob.service.JobLogService;
import com.qianlima.easyjob.service.JobService;
import com.qianlima.easyjob.statistics.JobStatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final JobService jobService;

    private final JobLogService jobLogService;

    private final JobStatisticsService statisticsService;

    public DashboardController(JobService jobService, JobLogService jobLogService, JobStatisticsService statisticsService) {
        this.jobService = jobService;
        this.jobLogService = jobLogService;
        this.statisticsService = statisticsService;

    }


    @GetMapping({"/", "/dashboard"})
    public String dashboard( @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "20") int pageSize,
                             Model model) {
        List<JobEntity> jobs = jobService.pageJobs(page, pageSize);
        model.addAttribute("jobs", jobs);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        return "dashboard";
    }

    @GetMapping("/jobs/{jobId}/logs")
    public String jobLogs(@PathVariable Long jobId, @RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "10") int pageSize, Model model) {
        JobEntity job = jobService.getJobById(jobId);
        List<JobLogEntity> logs = jobLogService.getJobLogs(jobId, page, pageSize);
        
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(7); // 最近7天的统计
        Map<String, Object> statistics = statisticsService.getJobStatistics(jobId, startTime, endTime);
        
        model.addAttribute("job", job);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("logs", logs);
        model.addAttribute("statistics", statistics);
        return "job-logs";
    }

}
