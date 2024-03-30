CREATE TABLE IF NOT EXISTS ${schema}.`family_account` (
    `account_id` bigint NOT NULL AUTO_INCREMENT COMMENT '账户编号',
    `name` varchar(30) DEFAULT '' COMMENT '账户别名',
    `type` tinyint(1) NOT NULL COMMENT '账户类型 1银行卡 2支付宝 3微信',
    `user_id` bigint NOT NULL COMMENT '所属用户id',
    `card_no` varchar(16) DEFAULT NULL COMMENT '银行卡号',
    `init_amount` decimal(10,2) NOT NULL COMMENT '账户初始余额 计算用',
    `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '账户余额',
    `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='家庭账户表';

ALTER TABLE ${schema}.`family_bill`
    ADD COLUMN `account_id` bigint(0) NOT NULL DEFAULT 1 COMMENT '本次开支所属账户id' AFTER `bill_id`;

ALTER TABLE ${schema}.`family_bill`
    ADD INDEX(`account_id`) USING BTREE;