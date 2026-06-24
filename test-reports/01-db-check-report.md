# 数据库检查与修复报告

**测试时间：** 2026-06-24 18:07

---

## 1. 读取到的配置

| 配置项 | 值 |
|--------|-----|
| 后端端口 | 8080 |
| 数据库地址 | localhost |
| 数据库端口 | 3306 |
| 数据库名 | tanku |
| 用户名 | root |
| spring.sql.init.mode | always |
| continue-on-error | true |
| JWT 配置 | 未配置 |

---

## 2. 数据库连接

成功连接到 `localhost:3306/tanku`

---

## 3. 表存在情况

| 表名 | 初始状态 | 修复后状态 |
|------|----------|------------|
| admin | 存在 | 存在 |
| company | 缺失 | **自动创建** |
| carbon_emission | 缺失 | **自动创建** |
| carbon_trade | 缺失 | **自动创建** |
| new_energy_project | 缺失 | **自动创建** |
| energy_saving_goal | 缺失 | **自动创建** |
| role | 缺失 | 未创建 |
| permission | 缺失 | 未创建 |
| role_permission | 缺失 | 未创建 |

---

## 4. init.sql 执行情况

直接执行 `init.sql` 因 MySQL 版本不支持 `ADD COLUMN IF NOT EXISTS` 语法在第 3 行失败。之后通过手动逐条执行 CREATE TABLE 语句成功创建 5 张业务表。

**已修复：** 将 init.sql 中 `ADD COLUMN IF NOT EXISTS` 改为 `ADD COLUMN`（配合 `continue-on-error: true` 使用）。

---

## 5. admin 表字段修复

### 修复前字段
id, username, name, userpwd, sex, tel, headurl, deleted (8 字段)

### 修复后字段（新增 5 字段）
| 新增字段 | 类型 | 说明 |
|----------|------|------|
| company_id | BIGINT NULL | 所属企业ID |
| role_id | BIGINT NULL | 角色ID |
| status | TINYINT DEFAULT 1 | 状态：0禁用，1启用 |
| create_time | DATETIME DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

所有字段均添加成功，无报错。

---

## 6. 可空字段修复

admin 表的 name/sex/tel/headurl 已经全部为 `YES NULL`，无需额外修复。

---

## 7. 数据量统计

| 表名 | 数据量 |
|------|--------|
| admin | 15 |
| company | 0 |
| carbon_emission | 0 |
| carbon_trade | 0 |
| new_energy_project | 0 |
| energy_saving_goal | 0 |

---

## 8. 测试账号

| 账号 | 密码 | 状态 |
|------|------|------|
| test_admin | 123456 | **已创建**（本次新增） |
| zhangsan | 123456 | 已存在 |
| lisi | 123456 | 已存在 |

---

## 9. 本批自动修复内容

1. 自动创建了 5 张缺失的业务表（company / carbon_emission / carbon_trade / new_energy_project / energy_saving_goal）
2. 为 admin 表新增了 5 个字段（company_id / role_id / status / create_time / update_time）
3. 修复了 init.sql 中 MySQL 不兼容的 `ADD COLUMN IF NOT EXISTS` 语法
4. 插入了测试账号 test_admin / 123456

---

## 10. 仍存在的问题

- role / permission / role_permission 表不存在（后端未开发对应模块）
- 5 张业务表均为空，无测试数据
- JWT 配置未发现，需在第 2 批验证
