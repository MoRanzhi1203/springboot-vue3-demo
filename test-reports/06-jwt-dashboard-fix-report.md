# JWT认证 + Dashboard 数据修复报告

**修复时间：** 2026-06-24 18:51  
**修复批次：** 第一批 P1 问题修复

---

## 1. 修复的 P1 问题

| 编号 | 问题 | 修复前 | 修复后 |
|------|------|--------|--------|
| U-01 | JWT 未接入 | 登录不返回 token，接口无拦截 | 登录返回 JWT，不带 token 返回 401 |
| U-02 | auth.js btoa 伪造 token | `'Bearer ' + btoa(JSON.stringify(...))` | 已删除，使用后端真实 JWT |
| U-03 | 接口无强制 Authorization | 所有接口可无认证访问 | 拦截器强制校验，放行白名单 |
| U-05 | Dashboard 数据全为 0 | 5张业务表均为空 | 插入 21 条测试数据，字段值 > 0 |

---

## 2. 新增后端文件

| 文件 | 说明 |
|------|------|
| `config/WebConfig.java` | 注册 JwtInterceptor，拦截 `/**`，放行登录/注册/Knife4j/Druid |
| `interceptor/JwtInterceptor.java` | 实现 HandlerInterceptor，校验 Authorization → 401 响应 |
| `utils/JwtUtils.java` | JJWT 0.12.6: generateToken / validateToken / parseToken |

## 3. 修改的文件

| 文件 | 操作 | 说明 |
|------|------|------|
| `pom.xml` | 修改 | 新增 jjwt-api / jjwt-impl / jjwt-jackson 0.12.6 |
| `application.yml` | 修改 | 新增 jwt.secret / jwt.expiration 配置 |
| `AdminController.java` | 修改 | login() 注入 JwtUtils，返回 `{token, user}` |
| `auth.js` | 修改 | 删除 btoa 伪造逻辑，`login(loginData)` 接收 `loginData.user` + `loginData.token` |

---

## 4. JWT 测试结果

| 测试项 | 结果 |
|--------|------|
| POST /admin/login → 返回 token | **PASS** — `code=200`, `data.token` 为 JWT |
| 不带 token 访问 GET /admin/list | **PASS** — HTTP 401 `{"code":401,"message":"未登录或token已过期"}` |
| 带 token 访问 GET /admin/list | **PASS** — code=200 |
| 带 token 访问 GET /dashboard/overview | **PASS** — code=200 |
| Knife4j doc.html 可访问 | **PASS** — 白名单放行 |
| POST /admin/login 可访问 | **PASS** — 白名单放行 |

---

## 5. 登录接口返回结构

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzM4NCJ9...",
    "user": {
      "id": 4,
      "username": "test_admin",
      "name": "测试管理员",
      "sex": "男",
      "tel": "13800000000",
      "headurl": null,
      "deleted": 0,
      "userpwd": null
    }
  }
}
```

---

## 6. Dashboard 数据验证

经过插入测试数据后：

| 字段 | 修复前 | 修复后 | 来源 |
|------|--------|--------|------|
| totalEmission | 0 | **10140.0000** | carbon_emission 表 8 条 SUM |
| companyCount | 0 | **3** | company 表 |
| reductionRate | 0.0 | **73.75** | energy_saving_goal 表 progress 平均值 |
| monthlyEmission | 12个0 | **1-6月有有效数据** | carbon_emission 表 2024年数据 |
| sourceData | 4个0 | **>0 的占比数据** | Mapper UNION ALL 查询 |

---

## 7. 测试数据插入

| 表 | 数据量 | 内容 |
|------|--------|------|
| company | 3 条 | 宝钢股份、华能国际、海螺水泥 |
| carbon_emission | 8 条 | 2024年1-6月 + 2025年1-2月 |
| carbon_trade | 3 条 | 配额交易、CCER 交易 |
| new_energy_project | 3 条 | 光伏、风电、余热发电 |
| energy_saving_goal | 4 条 | progress: 50/70/85/90 |

所有 INSERT 使用 `INSERT IGNORE`，重复执行不会报错。

---

## 8. mvn compile + npm run build

| 项目 | 结果 |
|------|------|
| mvn clean compile | **成功** |
| npm run build | **成功** (built in 1.33s) |

---

## 9. Authorization 校验白名单

| 路径 | 是否需要 token |
|------|----------------|
| /admin/login | 否 |
| /admin/register | 否 |
| /doc.html, /swagger-ui/**, /v3/api-docs/** | 否 |
| /druid/** | 否 |
| /error | 否 |
| 其余所有 /** | **是 — 无 token 返回 401** |

---

## 10. 仍未修复的问题（下一批）

| 编号 | 问题 |
|------|------|
| U-03 | admin/export 未实现 |
| U-04 | admin/import 缺少文件上传解析 |
| U-06 | role/permission/role_permission 表未创建 |
| U-07 | 日志管理模块未启动 |
| U-08 | 密码明文存储 |
