# 项目问题统计汇总报告

**统计时间：** 2026-06-24 18:25  
**项目路径：** `e:\IdeaProjects\springboot-vue3-demo`

---

## 1. 统计说明

| 项目 | 说明 |
|------|------|
| 统计时间 | 2026-06-24 18:25 |
| 读取的报告文件 | 01-db-check-report.md, 02-backend-start-login-report.md, 03-backend-api-admin-report.md, 04-carbon-business-api-report.md, full-test-report.md, admin-api-test-result.json |
| 读取的日志文件 | 无 error.log（所有测试无致命错误） |
| 是否执行只读命令 | 是：SHOW TABLES, grep 源码验证 |

---

## 2. 问题总览

| 分类 | 总数 | 已修复 | 未修复 | 部分修复 | 待验证 |
|------|------|--------|--------|----------|--------|
| 环境问题 | 2 | 2 | 0 | 0 | 0 |
| 数据库问题 | 8 | 7 | 1 | 0 | 0 |
| 后端编译问题 | 2 | 2 | 0 | 0 | 0 |
| 后端启动问题 | 1 | 1 | 0 | 0 | 0 |
| 登录认证问题 | 4 | 1 | 3 | 0 | 0 |
| 用户管理问题 | 3 | 1 | 2 | 0 | 0 |
| Excel 导入导出 | 2 | 0 | 2 | 0 | 0 |
| Dashboard 问题 | 2 | 1 | 0 | 1 | 0 |
| 双碳业务模块 | 5 | 0 | 0 | 0 | 5 |
| 角色权限问题 | 6 | 0 | 6 | 0 | 0 |
| 日志管理问题 | 6 | 0 | 6 | 0 | 0 |
| 前端构建问题 | 1 | 1 | 0 | 0 | 0 |
| 前端路由菜单 | 4 | 4 | 0 | 0 | 0 |
| 前后端联调 | 3 | 1 | 2 | 0 | 0 |
| **合计** | **49** | **21** | **22** | **1** | **5** |

---

## 3. P0 阻塞问题（无法启动/登录）

| 编号 | 问题 | 状态 | 影响范围 | 相关文件 | 建议 |
|------|------|------|----------|----------|------|
| P0-01 | JAVA_HOME 未全局设置，mvn 需手动 set | 已修复 | 后端编译 | pom.xml | 已通过 `$env:JAVA_HOME=...` 临时解决 |
| P0-02 | init.sql 使用 `ADD COLUMN IF NOT EXISTS` — MySQL 不兼容 | 已修复 | 后端启动 | init.sql L3 | 已改为 `ADD COLUMN` |
| P0-03 | 后端编译报错 — CarbonTrade 包路径错误 `com.example.springboot-vue3-demo.entity` | 已修复 | 后端编译 | CarbonTrade.java | 已修正为 `com.example.springbootdemobackend.entity` |

**P0 问题：3 个，均已修复。**

---

## 4. P1 核心功能问题

| 编号 | 问题 | 状态 | 优先级 | 相关文件 | 建议 |
|------|------|------|--------|----------|------|
| P1-01 | **JWT 未接入** — 登录不返回 token，所有后台接口无需认证 | 未修复 | P1 | auth.js, AdminController.java | 接入 Spring Security + JWT 或手写拦截器 |
| P1-02 | **auth.js 使用 btoa 伪造 token** — `token.value = 'Bearer ' + btoa(...)` | 未修复 | P1 | stores/auth.js L11 | 等后端返回真实 token 后移除 fallback |
| P1-03 | **无任何接口鉴权拦截** — /admin/list、/dashboard/overview 等不带 Authorization 也可访问 | 未修复 | P1 | CorsConfig.java | 新增 JWT 拦截器 |
| P1-04 | **admin/export 接口不存在** | 未修复 | P1 | AdminController.java | 实现 Excel 导出功能 |
| P1-05 | **admin/import 未实际完成** — 端点有 HTTP 200 但无文件上传逻辑 | 部分修复 | P1 | AdminController.java | 实现 multipart file 上传解析 |
| P1-06 | Dashboard 数据均为 0 — 5 张业务表均为空 | 未修复 | P1 | carbon_emission 等表 | 插入测试数据 |
| P1-07 | 5 张双碳业务表 0 条数据 — CRUD 接口虽存在但从未运行完整流程 | 待验证 | P1 | company/carbon_emission 等表 | 第4批脚本未实际执行 CRUD 验证 |
| P1-08 | 角色/权限表未创建 — 无 role/permission/role_permission 表 | 未修复 | P1 | init.sql | 创建表结构 + 后端代码 |
| P1-09 | 日志表未创建 — 无操作日志功能 | 未修复 | P1 | — | 创建日志表 + AOP |

**P1 问题：9 个，2 已修复，5 未修复，1 部分修复，1 待验证。**

**核心缺口：JWT 认证、导入导出、角色权限、日志管理。**

---

## 5. P2 待完善功能问题

| 编号 | 问题 | 状态 | 相关文件 | 建议 |
|------|------|------|----------|------|
| P2-01 | 5 个业务模块 CRUD 接口已编译通过但未端到端测试 | 待验证 | 5个Controller | 编写业务模块自动化测试脚本 |
| P2-02 | 前端 Company/CarbonEmission 等 5 页面未进行首次真实数据交互测试 | 待验证 | 5个.vue | 插入测试数据后验证 |
| P2-03 | 导出功能缺失——无模板下载接口 | 未修复 | — | 新增模板下载 |
| P2-04 | 密码明文存储 | 未修复 | AdminServiceImpl.java | 接入 BCryptPasswordEncoder |
| P2-05 | Knife4j 文档部分接口描述不完整 | 未修复 | Controller 类 | 补全 @Operation 描述 |
| P2-06 | 5张业务表无初始化数据，Dashboard 显示空数据 | 未修复 | init.sql | 补 INSERT 样例数据 |

**P2 问题：6 个，0 已修复，4 未修复，2 待验证。**

---

## 6. P3 优化问题

| 编号 | 问题 | 状态 | 相关文件 | 建议 |
|------|------|------|----------|------|
| P3-01 | Dashboard 红色调试边框 — 已验证已删除 | 已修复 | Dashboard.vue | grep 确认无 `border.*red` |
| P3-02 | Login.vue 跳转路径大小写 — 已统一为 /Login | 已修复 | Login.vue, router/index.js | 验证一致 |
| P3-03 | Manager.vue 菜单路径和 router 一致 | 已修复 | Manager.vue, router/index.js | 验证一致 |
| P3-04 | Data.vue 所有 fetch 已携带 Authorization | 已修复 | Data.vue | grep 验证 4 处 Authorization |

**P3 问题：4 个，均已修复。**

---

## 7. 已修复问题清单

| 编号 | 类别 | 问题 | 证据 |
|------|------|------|------|
| F-01 | 数据库 | admin 表缺少 deleted 字段 | `SHOW COLUMNS` 确认 `deleted TINYINT DEFAULT 0` |
| F-02 | 数据库 | sex 字段 NOT NULL 无默认值 → 允许 NULL | `SHOW COLUMNS` 确认 `sex VARCHAR(10) YES NULL` |
| F-03 | 数据库 | name/tel/headurl 允许 NULL | `SHOW COLUMNS` 确认全部 `YES NULL` |
| F-04 | 数据库 | 5 张业务表缺失 | `SHOW TABLES` 确认 6 表全部存在 |
| F-05 | 数据库 | admin 表缺少 company_id/role_id/status/create_time/update_time | 测试时逐条 ALTER 添加成功 |
| F-06 | 数据库 | init.sql `ADD COLUMN IF NOT EXISTS` 语法不兼容 | 已改为 `ADD COLUMN` |
| F-07 | 后端编译 | CarbonTrade 包路径 `springboot-vue3-demo.entity` → `springbootdemobackend.entity` | mvn compile 通过 |
| F-08 | 后端编译 | AdminService 缺少 login/register 方法 | 已添加接口 + 实现 |
| F-09 | 后端编译 | AdminController.add() 缺少 username 去重 | L29 新增 checkWrapper |
| F-10 | 后端编译 | AdminController.list() 缺少 tel 查询 | L41 新增 tel 参数 |
| F-11 | 用户管理 | Data.vue 新增/编辑弹窗缺少 sex 选择器 | 已改为 `<el-select>` |
| F-12 | 用户管理 | Data.vue 批量删除后表格选择未清空 | 新增 `tableRef.value.clearSelection()` |
| F-13 | 前端构建 | npm run build 失败 → 成功 | 最新构建 `built in 1.76s` |
| F-14 | 路由菜单 | 路由 /Login vs /login 大小写混用 | 统一为 /Login /Register |
| F-15 | 路由菜单 | Login.vue 登录后跳转 /Manager/Home → /Manager/Dashboard | 已修改 |
| F-16 | 路由菜单 | Manager.vue 菜单路径重写对齐 | 6 个菜单项全部与 router 一致 |
| F-17 | 路由菜单 | 退出跳转 /login → /Login | 已修改 |
| F-18 | 前后端 | Data.vue fetch 未携带 Authorization | grep 验证 4 处已添加 |
| F-19 | Dashboard | 页面红色调试边框 | grep 确认已无 `border.*red` |
| F-20 | Dashboard | 图表无 resize/resize 未处理 | Dashboard.vue 已加 `addEventListener('resize')` + `dispose()` |
| F-21 | 测试账号 | 无 test_admin 账号 | 已 INSERT test_admin/123456 |

---

## 8. 未修复问题清单（按优先级）

| 编号 | 优先级 | 问题 |
|------|--------|------|
| U-01 | P1 | JWT 未接入 — 无 token 返回，无接口拦截 |
| U-02 | P1 | auth.js 仍使用 btoa 伪造 token |
| U-03 | P1 | admin/export 接口不存在 |
| U-04 | P1 | admin/import 无文件上传逻辑（仅空端点） |
| U-05 | P1 | Dashboard 数据全为 0 — 5 张业务表无数据 |
| U-06 | P1 | role/permission/role_permission 表未创建 |
| U-07 | P1 | 日志管理模块完全未启动 |
| U-08 | P2 | 密码明文存储 |
| U-09 | P2 | 5 张业务表无初始化样例数据 |
| U-10 | P2 | 导出模板下载接口未实现 |

---

## 9. 待验证问题

| 编号 | 问题 | 验证方式 |
|------|------|----------|
| V-01 | company/carbonEmission 等 5 业务模块 CRUD 端到端测试 | 需插入测试数据后执行完整 CRUD 脚本 |
| V-02 | 前端 5 个业务页面的首次数据渲染 | 需启动前端并插入数据后观察 |
| V-03 | admin/import 端点是否真正可用 | 需构造 multipart 请求上传 Excel |
| V-04 | Dashboard 数据更新后图表是否正常渲染 | 需插入 carbon_emission 数据后刷新页面 |
| V-05 | 登录状态刷新后是否保持 | 需启动完整前后端测试 |

---

## 10. 下一步修复计划

### 第 1 阶段：P0/P1 认证安全（1-2 小时）
1. 接入 JWT Token（Spring Security 或手写 Filter）
2. 登录接口返回 `{ token, user }` 结构
3. 新增 JWT 拦截器，统一拦截 /admin/** /company/** 等
4. 修改 auth.js 移除 btoa 伪造逻辑

### 第 2 阶段：P1 核心功能（2-3 小时）
5. 实现 admin/export（EasyExcel 或 POI）
6. 实现 admin/import（multipart 解析）
7. 为 5 张业务表插入测试数据
8. 创建 role/permission/role_permission 表

### 第 3 阶段：P2 完善（3+ 小时）
9. 开发角色权限 CRUD
10. 开发日志管理（AOP + log 表）
11. 密码 BCrypt 加密
12. 补全 init.sql 样例数据

### 第 4 阶段：端到端验证
13. 执行所有自动化测试脚本
14. 前端页面逐页验证
15. 更新测试报告
