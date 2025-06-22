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

create table job_user
(
    status      int          not null,
    createdTime datetime(6)  null,
    id          bigint auto_increment
        primary key,
    updatedTime datetime(6)  null,
    email       varchar(255) null,
    password    varchar(255) not null,
    username    varchar(255) not null,
    constraint UK_sb8bbouer5wak8vyiiy4pf2bx
        unique (username)
);


-- 创建job表
CREATE TABLE IF NOT EXISTS `job_entity` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `job_name` varchar(255) NOT NULL COMMENT '任务名称',
    `job_group` varchar(255) NOT NULL COMMENT '任务分组',
    `cron_expression` varchar(255) NOT NULL COMMENT 'cron表达式',
    `job_class_name` varchar(255) NOT NULL COMMENT '任务类名',
    `job_data` text COMMENT '任务数据',
    `description` varchar(255) DEFAULT NULL COMMENT '描述信息',
    `status` varchar(20) NOT NULL DEFAULT 'NORMAL' COMMENT '任务状态',
    `created_time` datetime(6) DEFAULT NULL COMMENT '创建时间',
    `updated_time` datetime(6) DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_job_name_group` (`job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 创建job执行日志表
create table job_log_entity
(
    id            bigint auto_increment
        primary key,
    job_id        bigint        null comment 'job_id',
    startTime     datetime(6)   null comment '开始时间',
    endTime       datetime(6)   null comment '结束时间',
    executionTime bigint        null comment '执行时长(毫秒)',
    message       varchar(4000) null comment '执行信息',
    exceptionInfo varchar(255)  null comment '错误信息',
    jobClass      varchar(255)  not null,
    jobGroup      varchar(255)  not null,
    jobName       varchar(255)  not null,
    params        varchar(255)  null,
    status        varchar(255)  null,
    constraint FKsn4bestak4se0btf7mwjfxexo
        foreign key (job_id) references schedule_job (id)
);


-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `created_time` datetime(6) DEFAULT NULL COMMENT '创建时间',
    `updated_time` datetime(6) DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
