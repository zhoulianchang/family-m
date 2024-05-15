CREATE TABLE IF NOT EXISTS ${schema}.`family_file` (
    `file_id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件Id',
    `type` enum('FILE','DIR') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件类型，枚举。文件和目录',
    `name` varchar(32) NOT NULL COMMENT '文件名称',
    `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父节点',
    `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '祖级列表',
    `dept_id` bigint NOT NULL DEFAULT '0' COMMENT '所属部门id',
    `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(128) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`file_id`),
    KEY `del_flag` (`del_flag`),
    KEY `parent_id` (`parent_id`),
    KEY `type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件属性表';

CREATE TABLE IF NOT EXISTS ${schema}.`family_file_ext` (
    `file_id` bigint NOT NULL COMMENT 'family_file表的主键id',
    `file_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件的云存储路径（不含域名）',
    `place` enum('LOCAL','MINIO','ALIYUN') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'MINIO' COMMENT '文件存储位置',
    `file_size` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '文件大小,单位MB',
    `file_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件类型',
    PRIMARY KEY (`file_id`),
    CONSTRAINT `family_file_ext_ibfk_1` FOREIGN KEY (`file_id`) REFERENCES `family_file` (`file_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件属性扩展表';