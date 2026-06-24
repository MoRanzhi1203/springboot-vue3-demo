# 下一步修复计划

**生成时间：** 2026-06-24 18:25

---

## 优先级排序

```
P0 (阻塞) → 已清零 ✓
P1 (核心) → 9 项待处理
P2 (完善) → 6 项待处理
P3 (优化) → 已清零 ✓
```

---

## 第 1 轮：JWT 认证 + Token（P1 — 预计 1-2 小时）

| 步骤 | 任务 | 涉及文件 |
|------|------|----------|
| 1 | 引入 jjwt 依赖 | pom.xml |
| 2 | 创建 JwtUtil 工具类 | config/JwtUtil.java |
| 3 | 创建 JwtFilter 拦截器 | config/JwtFilter.java |
| 4 | Login 返回 `{token, user}` | AdminController.java |
| 5 | 拦截 /admin/** /company/** 等路径 | JwtFilter 配置 |
| 6 | 修改 auth.js 移除 btoa 伪造 | stores/auth.js L11 |
| 7 | 登录测试验证 token 可用 | test 脚本 |

**目标：登录返回真实 token，不带 token 的请求返回 401**

---

## 第 2 轮：用户导入导出 + 业务数据（P1 — 预计 2 小时）

| 步骤 | 任务 | 涉及文件 |
|------|------|----------|
| 1 | 实现 GET /admin/export | AdminController.java |
| 2 | 实现 POST /admin/import | AdminController.java |
| 3 | 添加 EasyExcel 依赖 | pom.xml |
| 4 | 为 5 张业务表 INSERT 样例数据 | init.sql |
| 5 | 验证 Dashboard 数据非零 | test 脚本 |
| 6 | 验证导入导出可用 | test 脚本 |

**目标：导入导出可操作，Dashboard 显示真实数据**

---

## 第 3 轮：角色权限（P1 — 预计 3+ 小时）

| 步骤 | 任务 | 涉及文件 |
|------|------|----------|
| 1 | 创建 role / permission / role_permission 表 | init.sql |
| 2 | 创建 Role / Permission 实体 | entity/ |
| 3 | 创建 Mapper + Service + Controller | mapper/ service/ controller/ |
| 4 | 前端角色管理页面 | views/Role.vue |
| 5 | 前端权限管理页面 | views/Permission.vue |
| 6 | 用户分配角色 | AdminController 扩展 |

---

## 第 4 轮：日志管理 + 密码加密（P2 — 预计 2 小时）

| 步骤 | 任务 | 涉及文件 |
|------|------|----------|
| 1 | 创建操作日志表 | init.sql |
| 2 | AOP 自动记录登录/增删改 | aspect/LogAspect.java |
| 3 | 日志查询页面 | views/Log.vue |
| 4 | BCrypt 密码加密 | AdminServiceImpl.java |
| 5 | 前端日志管理 CRUD | views/Log.vue |

---

## 第 5 轮：端到端验证

| 步骤 | 任务 |
|------|------|
| 1 | 执行 scripts/backend-admin-api-test.ps1 |
| 2 | 编写并执行 business-api-test.ps1 |
| 3 | 启动前后端，逐页验证 |
| 4 | 生成最终测试报告 |
