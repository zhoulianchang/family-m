ALTER TABLE ${schema}.`family_account`
    ADD COLUMN `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '启用状态，1启用 0禁用';