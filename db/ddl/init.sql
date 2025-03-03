CREATE TABLE `student` (
                           `id` bigint(64) unsigned NOT NULL COMMENT '学生表主键',
                           `stu_no` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '学生编号',
                           `name` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '学生姓名',
                           `sex` varchar(2) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '性别',
                           `height` int(4) DEFAULT NULL COMMENT '身高',
                           `weight` int(4) DEFAULT NULL COMMENT '体重',
                           `tenant_id` varchar(2) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '租户id',
                           `create_by` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
                           `update_by` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人',
                           `create_time` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
                           `update_time` datetime(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
                           `del_flag` smallint(1) DEFAULT '0' COMMENT '是否删除',
                           `status` smallint(1) DEFAULT '1' COMMENT '状态',
                           `dept_id` bigint(64) DEFAULT NULL COMMENT '部门id',
                           PRIMARY KEY (`id`),
                           KEY `STUDENT_INDEX1` (`stu_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='学生表';

CREATE TABLE `teacher` (
                           `id` bigint(64) unsigned NOT NULL COMMENT '教师表主键',
                           `editor_no` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '教师编号',
                           `name` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '教师姓名',
                           `sex` varchar(2) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '性别',
                           `mobile` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '手机号',
                           `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
                           `tenant_id` varchar(2) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '租户id',
                           `create_by` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
                           `update_by` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人',
                           `create_time` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
                           `update_time` datetime(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
                           `del_flag` smallint(1) DEFAULT '0' COMMENT '是否删除',
                           `status` smallint(1) DEFAULT '1' COMMENT '状态',
                           `dept_id` bigint(64) DEFAULT NULL COMMENT '部门id',
                           PRIMARY KEY (`id`),
                           KEY `TEACHER_INDEX1` (`editor_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='教师表';