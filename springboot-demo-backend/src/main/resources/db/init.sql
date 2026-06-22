-- ==================== 用户管理表 ====================
-- 兼容已有表：补充 deleted 逻辑删除列（列已存在时自动跳过）
ALTER TABLE `admin` ADD COLUMN IF NOT EXISTS `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除';

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
