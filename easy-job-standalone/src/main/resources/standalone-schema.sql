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

-- 创建用户表
create table job_user
(
    id          bigint not null auto_increment
        primary key,
    username    varchar(255) not null,
    password    varchar(255) not null,
    status      int          not null,
    createdTime datetime(6)  null,
    updatedTime datetime(6)  null,
    constraint UK_sb8bbouer5wak8vyiiy4pf2bx
        unique (username)
);


-- 创建job表
create table job_entity
(
    id             bigint  primary key auto_increment,
    jobGroup       varchar(255) not null COMMENT '任务分组',
    jobName        varchar(255) not null comment '任务名称',
    jobClass       varchar(255) not null COMMENT '任务类名',
    cronExpression varchar(255) not null COMMENT 'cron表达式',
    params         varchar(255) null COMMENT '任务参数',
    concurrent     bit          not null,
    status         bit          not null,
    description    varchar(255) null,
    createTime     datetime(6)  null comment '创建时间',
    updateTime     datetime(6)  null comment '更新时间',
    UNIQUE KEY `UK_job_group_name` (`jobGroup`, `jobName`)
);

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


