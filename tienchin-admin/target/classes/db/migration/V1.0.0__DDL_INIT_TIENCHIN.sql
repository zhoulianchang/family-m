/*
 Navicat MySQL Data Transfer

 Source Server         : local_8.0.26
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:23306
 Source Schema         : tienchin

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 13/03/2024 10:55:33
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tienchin_activity
-- ----------------------------
CREATE TABLE IF NOT EXISTS ${schema}.`tienchin_activity`
(
    `activity_id` bigint      NOT NULL AUTO_INCREMENT COMMENT '活动编号',
    `name`        varchar(64) NOT NULL COMMENT '活动名称',
    `channel_id`  int            DEFAULT NULL COMMENT '渠道ID',
    `info`        varchar(256)   DEFAULT NULL COMMENT '活动简介',
    `type`        tinyint(1) NOT NULL COMMENT '活动类型 1折扣劵 2代金劵',
    `discount`    decimal(10, 2) DEFAULT NULL COMMENT '折扣劵',
    `voucher`     decimal(10, 2) DEFAULT NULL COMMENT '代金劵',
    `begin_time`  datetime    NOT NULL COMMENT '活动开始时间',
    `end_time`    datetime    NOT NULL COMMENT '活动结束时间',
    `del_flag`    tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`   varchar(64)    DEFAULT '' COMMENT '创建者',
    `create_time` datetime       DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64)    DEFAULT '' COMMENT '更新者',
    `update_time` datetime       DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(256)   DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`activity_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='促销活动表';;

-- ----------------------------
-- Table structure for tienchin_channel
-- ----------------------------
CREATE TABLE IF NOT EXISTS ${schema}.`tienchin_channel`
(
    `channel_id`   bigint      NOT NULL AUTO_INCREMENT COMMENT '渠道编号',
    `channel_name` varchar(32) NOT NULL COMMENT '渠道名称',
    `status`       tinyint(1) NOT NULL DEFAULT '1' COMMENT '渠道状态 1正常 0禁用',
    `type`         tinyint(1) NOT NULL DEFAULT '1' COMMENT '渠道类型 1线上渠道 2线下渠道',
    `del_flag`     tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`    varchar(64)  DEFAULT '' COMMENT '创建者',
    `create_time`  datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(64)  DEFAULT '' COMMENT '更新者',
    `update_time`  datetime     DEFAULT NULL COMMENT '更新时间',
    `remark`       varchar(256) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`channel_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='渠道管理表';

CREATE TABLE IF NOT EXISTS ${schema}.`tienchin_course` (
    `course_id` bigint NOT NULL AUTO_INCREMENT COMMENT '课程id',
    `type` int NOT NULL COMMENT '课程类型 1舞蹈类 2游泳类 3拳击类',
    `name` varchar(32) NOT NULL COMMENT '课程名',
    `price` decimal(10,2) NOT NULL COMMENT '课程价格',
    `apply_to` int DEFAULT NULL COMMENT '课程适用人群',
    `info` varchar(256) DEFAULT NULL COMMENT '课程简介',
    `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(256) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`course_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程管理表';

CREATE TABLE IF NOT EXISTS ${schema}.`tienchin_clue` (
    `clue_id` bigint NOT NULL AUTO_INCREMENT COMMENT '线索编号',
    `name` varchar(32) NOT NULL COMMENT '客户名称',
    `channel_id` bigint DEFAULT NULL COMMENT '渠道编号',
    `activity_id` bigint DEFAULT NULL COMMENT '活动编号',
    `gender` int NOT NULL DEFAULT '0' COMMENT '客户性别 0男 1女',
    `age` int DEFAULT NULL COMMENT '客户年龄',
    `weixin` varchar(32) DEFAULT NULL COMMENT '客户微信',
    `qq` varchar(16) DEFAULT NULL COMMENT '客户qq',
    `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户手机号',
    `level` int DEFAULT NULL COMMENT '客户意向等级 1近期报名 2打算报名，考虑中 3了解一下 4打酱油',
    `subject` int DEFAULT NULL COMMENT '私教课程',
    `status` int NOT NULL COMMENT '线索状态 1已分配 2跟进中 3回收 4伪线索',
    `fail_count` int NOT NULL DEFAULT '0' COMMENT '伪线索失败次数，最大3次',
    `next_time` datetime DEFAULT NULL COMMENT '下次跟进时间',
    `end_time` datetime DEFAULT NULL COMMENT '线索失效时间',
    `transfer` tinyint(1) DEFAULT NULL COMMENT '线索是否需要转派',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(256) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`clue_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='线索管理表';

CREATE TABLE IF NOT EXISTS ${schema}.`tienchin_follow_record` (
    `record_id` bigint NOT NULL AUTO_INCREMENT COMMENT '跟进记录编号',
    `type` int NOT NULL COMMENT '跟进记录类型 1线索 2商机',
    `assign_id` bigint NOT NULL COMMENT '线索或者商机的id',
    `info` varchar(256) DEFAULT NULL COMMENT '跟进记录信息',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(256) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`record_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='跟进记录表';

CREATE TABLE IF NOT EXISTS ${schema}.`tienchin_assignment` (
    `assignment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '分配编号',
    `type` int NOT NULL COMMENT '跟进记录类型 1线索 2商机',
    `assign_id` bigint NOT NULL COMMENT '线索或者商机的id',
    `user_id` bigint DEFAULT NULL COMMENT '所属用户id',
    `user_name` varchar(30) DEFAULT NULL COMMENT '所属用户名称',
    `dept_id` bigint DEFAULT NULL COMMENT '所属用户的部门id',
    `latest` tinyint(1) DEFAULT NULL COMMENT '是否是当前最新分配人',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(256) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`assignment_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='分配表';
SET
FOREIGN_KEY_CHECKS = 1;