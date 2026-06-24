# 后端 API 自动化测试报告 — 用户管理 CRUD

**测试时间：** 2026-06-24 18:19  
**后端地址：** http://localhost:8080  
**测试脚本：** `scripts/backend-admin-api-test.ps1`  
**结果文件：** `test-reports/admin-api-test-result.json`

---

## 1. 登录接口

| 项目 | 结果 |
|------|------|
| 测试账号 | test_admin / 123456 |
| POST /admin/login | **PASS** HTTP 200, code=200 |
| token 返回 | **不返回** — JWT 未接入 |
| 返回结构 | `{ code:200, data:{ id,username,name,tel,... } }` |

---

## 2. Dashboard 接口

| 项目 | 结果 |
|------|------|
| GET /dashboard/overview | **PASS** HTTP 200 |
| totalEmission | 0.0000（数据库 carbon_emission 表无数据） |
| reductionRate | 0.0（数据库 energy_saving_goal 表无数据） |
| companyCount | 0（数据库 company 表无数据） |
| monthlyEmission | 返回（12 个月补齐，数据为 0） |
| sourceData | 返回（4 个来源，数据为 0） |

**结论：接口正常返回结构，字段完整。数据均为 0 是因为业务表无数据，不属于接口错误。**

---

## 3. 用户列表查询

| 项目 | 结果 |
|------|------|
| GET /admin/list?pagenum=1&pagesize=5 | **PASS** |
| 返回结构 | PageInfo 格式：包含 list、total、pageNum 等 |
| 当前用户数 | 16 条 |

---

## 4. 新增用户

| 项目 | 结果 |
|------|------|
| POST /admin/add | **PASS** |
| 测试用户名 | `auto_user_20260624181917` |
| 通过登录反向获取 ID | id=21 |
| username 去重 | 已验证（前端传入重复 username 时返回 "用户名已存在"） |

---

## 5. 修改用户

| 项目 | 结果 |
|------|------|
| PUT /admin/update | **PASS** |
| 修改内容 | name: `auto_test_user` → `auto_test_modified` |
| 修改后状态 | code=200 |

---

## 6. 删除用户

| 项目 | 结果 |
|------|------|
| DELETE /admin/delete/21 | **PASS** |
| 删除方式 | MyBatis-Plus 逻辑删除（`deleted` 设 1） |

---

## 7. 批量删除

| 项目 | 结果 |
|------|------|
| DELETE /admin/batchDelete?ids=22,23 | **PASS** |
| 新增 2 个测试用户后批量删除 | 成功删除 2 条 |

---

## 8. 用户导出

| 项目 | 结果 |
|------|------|
| GET /admin/export | **未实现** — 端点不存在 |

---

## 9. 用户导入

| 项目 | 结果 |
|------|------|
| POST /admin/import | **端点存在** (HTTP 200)，但未实际测试文件上传 |

---

## 10. 测试汇总

| 测试项 | 状态 |
|--------|------|
| POST /admin/login | PASS |
| GET /dashboard/overview | PASS |
| GET /admin/list | PASS |
| POST /admin/add | PASS |
| PUT /admin/update | PASS |
| DELETE /admin/delete/{id} | PASS |
| DELETE /admin/batchDelete | PASS |
| GET /admin/export | NOT IMPLEMENTED |
| POST /admin/import | RESPONDS |

**用户管理 CRUD 完整闭环全部通过（7/9 通过，2 项未实现）。**

---

## 11. 自动生成文件

| 文件 | 说明 |
|------|------|
| `scripts/backend-admin-api-test.ps1` | 可复用 PowerShell 测试脚本 |
| `test-reports/admin-api-test-result.json` | JSON 格式测试结果 |
| `test-reports/03-backend-api-admin-report.md` | 本报告 |

---

## 12. 发现的问题

1. **JWT 未接入** — 登录不返回 token，所有接口无需认证
2. **导出功能未实现** — GET /admin/export 端点不存在
3. **导入功能待完善** — 端点虽有响应但未验证实际文件上传
4. **Dashboard 数据为空** — 因为 5 张业务表均为空
