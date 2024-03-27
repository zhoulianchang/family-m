CREATE TABLE IF NOT EXISTS ${schema}.`family_bill` (
    `bill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '账单编号',
    `amount` decimal(10,2) NOT NULL COMMENT '账单金额',
    `user_name` varchar(30) DEFAULT NULL COMMENT '为谁花的',
    `type` tinyint(1) NOT NULL COMMENT '账单分类',
    `flow` tinyint(1) NOT NULL COMMENT '资金流向 1进 2出',
    `pay_time` datetime NOT NULL COMMENT '账单支付日期',
    `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
PRIMARY KEY (`bill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS ${schema}.`family_favor` (
    `favor_id` bigint NOT NULL AUTO_INCREMENT COMMENT '人情薄ID',
    `relation_id` bigint DEFAULT NULL COMMENT '关联自身id，即哪两笔人情账是平账的',
    `user_name` varchar(30) NOT NULL COMMENT '送/被送礼人',
    `flow` tinyint(1) NOT NULL COMMENT '资金流向',
    `favor_time` datetime NOT NULL COMMENT '人情时间',
    `amount` decimal(10,2) NOT NULL COMMENT '礼金金额',
    `balanced` int NOT NULL DEFAULT '1' COMMENT '是否平账 1不需要 2已平账 3未平账',
    `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`favor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;