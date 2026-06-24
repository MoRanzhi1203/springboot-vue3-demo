# 已修复问题清单

**统计时间：** 2026-06-24 18:25  
**已修复数量：** 21 个

---

## 数据库问题（7 个）

| 编号 | 问题 | 证据 |
|------|------|------|
| F-01 | admin 表缺少 deleted 字段 | SHOW COLUMNS: `deleted TINYINT DEFAULT 0` |
| F-02 | sex 字段 NOT NULL 无默认值 | SHOW COLUMNS: `sex VARCHAR(10) YES NULL` |
| F-03 | name/tel/headurl 不允许 NULL | SHOW COLUMNS: 全部 `YES NULL` |
| F-04 | 5 张业务表缺失 | SHOW TABLES: 6 表全部存在 |
| F-05 | admin 表缺少 5 个扩展字段 | company_id/role_id/status/create_time/update_time 已添加 |
| F-06 | init.sql ADD COLUMN IF NOT EXISTS | 已改为 ADD COLUMN |
| F-07 | 测试账号缺失 | test_admin/123456 已创建 |

## 后端编译（4 个）

| 编号 | 问题 | 证据 |
|------|------|------|
| F-08 | CarbonTrade 包路径错误 | mvn compile 通过 |
| F-09 | AdminService 缺少 login/register | 已添加接口+实现 |
| F-10 | add() 缺少 username 去重 | checkWrapper 已添加 |
| F-11 | list() 缺少 tel 查询 | @RequestParam tel 已添加 |

## 用户管理前端（2 个）

| 编号 | 问题 | 证据 |
|------|------|------|
| F-12 | Data.vue 弹窗缺少 sex 选择器 | 已改为 el-select |
| F-13 | 批量删除后选择未清空 | tableRef.clearSelection() 已添加 |

## 前端构建路由（5 个）

| 编号 | 问题 | 证据 |
|------|------|------|
| F-14 | npm run build 失败 | built in 1.76s |
| F-15 | /Login /login 大小写混用 | 统一为 /Login /Register |
| F-16 | 登录跳转 /Manager/Home | 改为 /Manager/Dashboard |
| F-17 | Manager.vue 菜单路径不一致 | 6 项全部对齐 |
| F-18 | 退出跳转路径错误 | 改为 /Login |

## 前后端联调（1 个）

| 编号 | 问题 | 证据 |
|------|------|------|
| F-19 | Data.vue fetch 未带 Authorization | grep 验证 4 处已添加 |

## Dashboard（2 个）

| 编号 | 问题 | 证据 |
|------|------|------|
| F-20 | 红色调试边框 | grep 确认无 `border.*red` |
| F-21 | ECharts resize/dispose | addEventListener + onBeforeUnmount 已添加 |
