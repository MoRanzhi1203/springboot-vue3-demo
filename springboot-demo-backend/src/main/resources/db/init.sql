-- ==================== 用户管理表 ====================
-- 兼容已有表：补充 deleted 逻辑删除列
ALTER TABLE `admin` ADD COLUMN `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除';
-- 兼容旧表：确保 name/sex/tel/headurl 允许 NULL
ALTER TABLE `admin` MODIFY COLUMN `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名';
ALTER TABLE `admin` MODIFY COLUMN `sex` VARCHAR(10) DEFAULT NULL COMMENT '性别';
ALTER TABLE `admin` MODIFY COLUMN `tel` VARCHAR(20) DEFAULT NULL COMMENT '电话';
ALTER TABLE `admin` MODIFY COLUMN `headurl` VARCHAR(255) DEFAULT NULL COMMENT '头像地址';
ALTER TABLE `admin` MODIFY COLUMN `userpwd` VARCHAR(100) NOT NULL COMMENT '密码';

CREATE TABLE IF NOT EXISTS `admin` (
    `id`       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50)  NOT NULL                COMMENT '账号',
    `userpwd`  VARCHAR(100) NOT NULL                COMMENT '密码',
    `name`     VARCHAR(50)  DEFAULT NULL            COMMENT '姓名',
    `sex`      VARCHAR(10)  DEFAULT NULL            COMMENT '性别',
    `tel`      VARCHAR(20)  DEFAULT NULL            COMMENT '电话',
    `headurl`  VARCHAR(255) DEFAULT NULL            COMMENT '头像地址',
    `deleted`  TINYINT      DEFAULT 0               COMMENT '逻辑删除(0=未删除,1=已删除)',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户管理表';

-- ==================== 样例数据 ====================
INSERT IGNORE INTO `admin` (`id`, `username`, `userpwd`, `name`, `sex`, `tel`, `headurl`) VALUES
(1,  'zhangsan', '123456', '张三',   '男', '13800001111', 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhangsan'),
(2,  'lisi',     '123456', '李四',   '男', '13800002222', 'https://api.dicebear.com/7.x/avataaars/svg?seed=lisi'),
(3,  'wangwu',   '123456', '王五',   '男', '13800003333', 'https://api.dicebear.com/7.x/avataaars/svg?seed=wangwu'),
(4,  'zhaoliu',  '123456', '赵六',   '女', '13800004444', 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhaoliu'),
(5,  'sunqi',    '123456', '孙七',   '女', '13800005555', 'https://api.dicebear.com/7.x/avataaars/svg?seed=sunqi'),
(6,  'zhouba',   '123456', '周八',   '男', '13800006666', 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhouba'),
(7,  'wujiu',    '123456', '吴九',   '女', '13800007777', 'https://api.dicebear.com/7.x/avataaars/svg?seed=wujiu'),
(8,  'zhengshi', '123456', '郑十',   '男', '13800008888', 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhengshi'),
(9,  'liuyi',    '123456', '刘一',   '女', '13800009999', 'https://api.dicebear.com/7.x/avataaars/svg?seed=liuyi'),
(10, 'chener',   '123456', '陈二',   '男', '13800000000', 'https://api.dicebear.com/7.x/avataaars/svg?seed=chener'),
(11, 'huangsan', '123456', '黄三',   '女', '13800001010', 'https://api.dicebear.com/7.x/avataaars/svg?seed=huangsan'),
(12, 'linsi',    '123456', '林四',   '男', '13800002020', 'https://api.dicebear.com/7.x/avataaars/svg?seed=linsi');

-- ==================== 企业表 ====================
CREATE TABLE IF NOT EXISTS `company` (
    `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `company_name`     VARCHAR(100) NOT NULL                COMMENT '企业名称',
    `company_code`     VARCHAR(50)  DEFAULT NULL            COMMENT '企业编码',
    `industry`         VARCHAR(50)  DEFAULT NULL            COMMENT '行业',
    `address`          VARCHAR(255) DEFAULT NULL            COMMENT '地址',
    `contact_person`   VARCHAR(50)  DEFAULT NULL            COMMENT '联系人',
    `contact_phone`    VARCHAR(20)  DEFAULT NULL            COMMENT '联系电话',
    `registered_capital` DECIMAL(20,2) DEFAULT NULL         COMMENT '注册资本',
    `establish_date`   DATE         DEFAULT NULL            COMMENT '成立日期',
    `emission_quota`   DECIMAL(20,2) DEFAULT NULL           COMMENT '碳排放配额',
    `status`           VARCHAR(20)  DEFAULT '正常'         COMMENT '状态',
    `create_time`      DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业表';

-- ==================== 碳排放表 ====================
CREATE TABLE IF NOT EXISTS `carbon_emission` (
    `id`                BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `company_id`        BIGINT   DEFAULT NULL            COMMENT '企业ID',
    `emission_year`     INT      DEFAULT NULL            COMMENT '排放年份',
    `emission_month`    INT      DEFAULT NULL            COMMENT '排放月份',
    `scope1_emission`   DECIMAL(20,4) DEFAULT NULL       COMMENT '范围1排放(直接)',
    `scope2_emission`   DECIMAL(20,4) DEFAULT NULL       COMMENT '范围2排放(间接)',
    `scope3_emission`   DECIMAL(20,4) DEFAULT NULL       COMMENT '范围3排放(其他)',
    `total_emission`    DECIMAL(20,4) DEFAULT NULL       COMMENT '总排放量',
    `data_source`       VARCHAR(50)  DEFAULT NULL        COMMENT '数据来源',
    `verification_status` VARCHAR(20) DEFAULT '未核查'   COMMENT '核查状态',
    `remark`            VARCHAR(500) DEFAULT NULL         COMMENT '备注',
    `create_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='碳排放表';

-- ==================== 碳交易表 ====================
CREATE TABLE IF NOT EXISTS `carbon_trade` (
    `id`              BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `trade_no`        VARCHAR(50)  DEFAULT NULL         COMMENT '交易编号',
    `seller_company_id` BIGINT     DEFAULT NULL         COMMENT '卖方企业ID',
    `buyer_company_id`  BIGINT     DEFAULT NULL         COMMENT '买方企业ID',
    `trade_type`      VARCHAR(20)  DEFAULT NULL         COMMENT '交易类型',
    `trade_quantity`  DECIMAL(20,4) DEFAULT NULL        COMMENT '交易数量',
    `trade_price`     DECIMAL(20,4) DEFAULT NULL        COMMENT '交易价格',
    `total_amount`    DECIMAL(20,2) DEFAULT NULL        COMMENT '总金额',
    `trade_date`      DATE         DEFAULT NULL         COMMENT '交易日期',
    `trade_platform`  VARCHAR(50)  DEFAULT NULL         COMMENT '交易平台',
    `status`          VARCHAR(20)  DEFAULT '进行中'     COMMENT '状态',
    `remark`          VARCHAR(500) DEFAULT NULL         COMMENT '备注',
    `create_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='碳交易表';

-- ==================== 新能源项目表 ====================
CREATE TABLE IF NOT EXISTS `new_energy_project` (
    `id`               BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `project_name`     VARCHAR(100) NOT NULL            COMMENT '项目名称',
    `project_code`     VARCHAR(50)  DEFAULT NULL        COMMENT '项目编码',
    `company_id`       BIGINT      DEFAULT NULL         COMMENT '企业ID',
    `project_type`     VARCHAR(50)  DEFAULT NULL        COMMENT '项目类型',
    `capacity`         DECIMAL(20,4) DEFAULT NULL       COMMENT '装机容量',
    `annual_generation` DECIMAL(20,4) DEFAULT NULL      COMMENT '年发电量',
    `investment_amount` DECIMAL(20,2) DEFAULT NULL      COMMENT '投资金额',
    `start_date`       DATE         DEFAULT NULL        COMMENT '开始日期',
    `completion_date`  DATE         DEFAULT NULL        COMMENT '完成日期',
    `location`         VARCHAR(255) DEFAULT NULL        COMMENT '地点',
    `status`           VARCHAR(20)  DEFAULT '在建'      COMMENT '状态',
    `description`      TEXT         DEFAULT NULL        COMMENT '描述',
    `create_time`      DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新能源项目表';

-- ==================== 节能减排目标表 ====================
CREATE TABLE IF NOT EXISTS `energy_saving_goal` (
    `id`              BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `company_id`      BIGINT      DEFAULT NULL         COMMENT '企业ID',
    `goal_type`       VARCHAR(50) DEFAULT NULL         COMMENT '目标类型',
    `target_year`     INT         DEFAULT NULL         COMMENT '目标年份',
    `baseline_year`   INT         DEFAULT NULL         COMMENT '基准年份',
    `baseline_value`  DECIMAL(20,4) DEFAULT NULL       COMMENT '基准值',
    `target_value`    DECIMAL(20,4) DEFAULT NULL       COMMENT '目标值',
    `actual_value`    DECIMAL(20,4) DEFAULT NULL       COMMENT '实际值',
    `target_unit`     VARCHAR(20) DEFAULT NULL         COMMENT '目标单位',
    `progress`        DECIMAL(5,2) DEFAULT 0           COMMENT '完成进度百分比',
    `status`          VARCHAR(20) DEFAULT '进行中'     COMMENT '状态',
    `description`     TEXT        DEFAULT NULL         COMMENT '描述',
    `create_time`     DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='节能减排目标表';

-- ==================== 角色表 ====================
CREATE TABLE IF NOT EXISTS `role` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name`   VARCHAR(50)  NOT NULL                COMMENT '角色名称',
    `role_code`   VARCHAR(50)  NOT NULL                COMMENT '角色编码',
    `description` VARCHAR(200) DEFAULT NULL            COMMENT '角色描述',
    `sort_order`  INT          DEFAULT 0               COMMENT '排序',
    `status`      TINYINT      DEFAULT 1               COMMENT '状态：0禁用，1启用',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- ==================== 权限表 ====================
CREATE TABLE IF NOT EXISTS `permission` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `permission_name` VARCHAR(50)  NOT NULL                COMMENT '权限名称',
    `permission_code` VARCHAR(100) NOT NULL                COMMENT '权限编码',
    `module`          VARCHAR(50)  DEFAULT NULL            COMMENT '所属模块',
    `parent_id`       BIGINT       DEFAULT 0               COMMENT '父权限ID',
    `path`            VARCHAR(200) DEFAULT NULL            COMMENT '权限路径',
    `permission_type` VARCHAR(20)  DEFAULT 'button'        COMMENT '权限类型',
    `icon`            VARCHAR(50)  DEFAULT NULL            COMMENT '图标',
    `sort_order`      INT          DEFAULT 0               COMMENT '排序',
    `status`          TINYINT      DEFAULT 1               COMMENT '状态',
    `create_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_code` (`permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- ==================== 角色权限关联表 ====================
CREATE TABLE IF NOT EXISTS `role_permission` (
    `id`            BIGINT   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id`       BIGINT   NOT NULL                COMMENT '角色ID',
    `permission_id` BIGINT   NOT NULL                COMMENT '权限ID',
    `create_time`   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- ==================== 初始化角色数据 ====================
INSERT IGNORE INTO `role` (`role_name`, `role_code`, `description`, `sort_order`, `status`) VALUES
('超级管理员', 'super_admin', '系统最高权限管理者', 1, 1),
('企业管理员', 'company_admin', '企业管理权限', 2, 1),
('数据管理员', 'data_admin', '数据管理权限', 3, 1),
('查看者', 'viewer', '只读权限', 4, 1);

-- ==================== 初始化权限数据 ====================
INSERT IGNORE INTO `permission` (`permission_name`, `permission_code`, `module`, `permission_type`, `sort_order`, `status`) VALUES
('数据概览', 'dashboard:view', '数据概览', 'button', 1, 1),
('用户查询', 'admin:list', '系统管理', 'button', 10, 1),
('用户新增', 'admin:add', '系统管理', 'button', 11, 1),
('用户修改', 'admin:update', '系统管理', 'button', 12, 1),
('用户删除', 'admin:delete', '系统管理', 'button', 13, 1),
('企业查询', 'company:list', '企业管理', 'button', 20, 1),
('企业新增', 'company:add', '企业管理', 'button', 21, 1),
('企业修改', 'company:update', '企业管理', 'button', 22, 1),
('企业删除', 'company:delete', '企业管理', 'button', 23, 1),
('碳排放查询', 'carbonEmission:list', '碳排放管理', 'button', 30, 1),
('碳排放新增', 'carbonEmission:add', '碳排放管理', 'button', 31, 1),
('碳排放修改', 'carbonEmission:update', '碳排放管理', 'button', 32, 1),
('碳排放删除', 'carbonEmission:delete', '碳排放管理', 'button', 33, 1),
('碳交易查询', 'carbonTrade:list', '碳交易管理', 'button', 40, 1),
('新能源项目查询', 'newEnergyProject:list', '新能源管理', 'button', 50, 1),
('节能目标查询', 'energySavingGoal:list', '节能减排管理', 'button', 60, 1),
('角色查询', 'role:list', '系统管理', 'button', 70, 1),
('角色新增', 'role:add', '系统管理', 'button', 71, 1),
('角色修改', 'role:update', '系统管理', 'button', 72, 1),
('角色删除', 'role:delete', '系统管理', 'button', 73, 1),
('权限查询', 'permission:list', '系统管理', 'button', 80, 1);

-- ==================== 初始化超级管理员权限 ====================
INSERT IGNORE INTO `role_permission` (`role_id`, `permission_id`)
SELECT r.id, p.id FROM `role` r CROSS JOIN `permission` p WHERE r.role_code = 'super_admin' AND p.status = 1;
