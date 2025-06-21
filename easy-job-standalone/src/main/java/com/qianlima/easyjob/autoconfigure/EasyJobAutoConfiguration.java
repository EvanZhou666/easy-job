package com.qianlima.easyjob.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ComponentScan(basePackages = "com.qianlima.easyjob")
@EnableJpaRepositories("com.qianlima.easyjob.auth.repository") // 明确指定包路径
@EntityScan(value = {"com.qianlima.easyjob.auth.entity", "com.qianlima.easyjob.entity"}) // 确保实体类被扫描
public class EasyJobAutoConfiguration {

//    @Bean
//    @ConditionalOnMissingBean
//    public JobService jobService() {
//        return new JobServiceImpl();
//    }

}