package com.qianlima.easyjob.job;

import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class SampleJob extends BaseJob {
    @Override
    protected void doExecute(JobExecutionContext context) throws Exception {
        // 这里实现具体的任务逻辑
        System.out.println("Sample job is running at: " + System.currentTimeMillis());
        // 可以通过context获取传入的参数
        String params = context.getJobDetail().getJobDataMap().getString("params");
        if (params != null) {
            System.out.println("Job parameters: " + params);
        }
        
        // 模拟任务执行
        Thread.sleep(1000);
    }
}
