# 后端启动与登录测试报告

**测试时间：** 2026-06-24 18:11

---

## 1. 后端编译

| 项目 | 结果 |
|------|------|
| mvn clean compile | **成功** |
| JAVA_HOME | C:\Program Files\Java\jdk-21.0.10 |

---

## 2. 后端启动

| 项目 | 结果 |
|------|------|
| 启动命令 | mvn spring-boot:run |
| 启动端口 | 8080 |
| 启动耗时 | 约 5.4 秒 |
| 启动结果 | **成功** — Tomcat started on port 8080 |

---

## 3. Knife4j 文档

| 项目 | 结果 |
|------|------|
| 访问地址 | http://localhost:8080/doc.html |
| HTTP 状态码 | 200 |
| 页面大小 | 1892 字节 |
| 可访问 | **是** |

---

## 4. 登录接口测试

### 测试账号

使用 `test_admin / 123456`

### 请求

```
POST http://localhost:8080/admin/login
{"username":"test_admin","userpwd":"123456"}
```

### 响应

| 项目 | 值 |
|------|-----|
| HTTP 状态码 | 200 |
| result.code | 200 |
| 返回用户对象 | 包含 id, username, name, tel 等 |
| userpwd 已清除 | null（安全处理正确） |

### 实际返回结构

```json
{"code":200,"data":{"id":X,"username":"test_admin","name":"测试管理员","tel":"13800000000",...}}
```

**结论：登录接口正常，但不返回 token 字段。JWT 未接入。**

---

## 5. Dashboard 接口测试

| 项目 | 值 |
|------|-----|
| GET /dashboard/overview | HTTP 200 |
| result.code | 200 |
| 是否需要登录 | 否（无拦截器） |

---

## 6. JWT / Authorization 测试

| 测试项 | 结果 |
|--------|------|
| 登录返回 token | **不返回** — JWT 未接入 |
| 不带 token 访问 /admin/list | HTTP 200 正常返回 |
| 不带 token 访问 /dashboard/overview | HTTP 200 正常返回 |
| 接口是否强制拦截 | **否** — 所有后台接口可在无 Authorization 下访问 |

---

## 7. 自动修复内容

1. init.sql 语法修复（移除 ADD COLUMN IF NOT EXISTS — MySQL 不兼容）
2. admin 表新增 company_id / role_id / status / create_time / update_time 5 个字段
3. 创建测试账号 test_admin

---

## 8. 发现的问题

- JWT 未接入，所有后台接口无认证保护
- 登录接口返回的是用户对象，无 token 结构
- 前端 auth.js 使用 Base64 生成 mock token，与后端不匹配
