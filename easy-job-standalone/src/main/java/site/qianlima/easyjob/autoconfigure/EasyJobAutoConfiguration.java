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

package site.qianlima.easyjob.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.quartz.JobStoreType;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Duration;
import java.util.Map;

@AutoConfiguration(before = QuartzAutoConfiguration.class)
@ComponentScan(basePackages = "site.qianlima.easyjob")
@EnableJpaRepositories("site.qianlima.easyjob.auth.repository") // 明确指定包路径
@EntityScan(value = {"site.qianlima.easyjob.auth.entity", "site.qianlima.easyjob.entity"}) // 确保实体类被扫描
public class EasyJobAutoConfiguration {

    private static final String SCHEDULER_NAME = "easy-job-scheduler";

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public QuartzProperties quartzProperties() {
        QuartzProperties properties = new QuartzProperties();
        properties.setJobStoreType(JobStoreType.MEMORY);
        properties.setSchedulerName(SCHEDULER_NAME);
        properties.setAutoStartup(true);
        properties.setStartupDelay(Duration.ofSeconds(3));
        properties.setWaitForJobsToCompleteOnShutdown(true);
        properties.setOverwriteExistingJobs(true);
        Map<String, String> map = properties.getProperties();
        map.put("org.quartz.scheduler.instanceName",  SCHEDULER_NAME);
        map.put("org.quartz.scheduler.instanceId", "AUTO");
        map.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        map.put("org.quartz.threadPool.threadCount", "64");
        map.put("org.quartz.scheduler.idleWaitTime", "3000");
        map.put("org.quartz.scheduler.batchTriggerAcquisitionFireAheadTimeWindow", "0");
        map.put("org.quartz.jobStore.misfireThreshold", "60000");
        map.put("org.quartz.scheduler.batchTriggerAcquisitionMaxCount", "16");
        return properties;
    }

}