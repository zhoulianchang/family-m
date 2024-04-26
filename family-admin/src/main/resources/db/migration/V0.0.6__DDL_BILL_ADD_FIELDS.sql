ALTER TABLE ${schema}.`family_bill`
    ADD COLUMN `dept_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '所属部门id' AFTER `account_id`;
ALTER TABLE ${schema}.`family_account`
    ADD COLUMN `dept_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '所属部门id' AFTER `user_id`;