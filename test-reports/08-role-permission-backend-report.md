# 角色权限后端接口测试报告

**测试时间：** 2026-06-24  
**编译结果：** mvn clean compile **成功**

---

## 1. 新增类清单

| 类型 | 类名 | 文件 |
|------|------|------|
| Entity | Role | entity/Role.java |
| Entity | Permission | entity/Permission.java |
| Entity | RolePermission | entity/RolePermission.java |
| Mapper | RoleMapper | mapper/RoleMapper.java |
| Mapper | PermissionMapper | mapper/PermissionMapper.java |
| Mapper | RolePermissionMapper | mapper/RolePermissionMapper.java |
| Service | RoleService | service/RoleService.java |
| Service | PermissionService | service/PermissionService.java |
| Service | RolePermissionService | service/RolePermissionService.java |
| ServiceImpl | RoleServiceImpl | service/impl/RoleServiceImpl.java |
| ServiceImpl | PermissionServiceImpl | service/impl/PermissionServiceImpl.java |
| ServiceImpl | RolePermissionServiceImpl | service/impl/RolePermissionServiceImpl.java |
| Controller | RoleController | controller/RoleController.java |
| Controller | PermissionController | controller/PermissionController.java |

---

## 2. 角色接口测试结果（8 个接口）

| 编号 | 接口 | 测试 | 结果 |
|------|------|------|------|
| 1 | 不带 token 访问 /role/list | 应 401 | **PASS** — 401 |
| 2 | GET /role/list?pagenum=1&pagesize=10 | 带 token | **PASS** — code=200 |
| 3 | POST /role/add | 新增 test_code | **PASS** — code=200 |
| 4 | GET /role/all | 获取所有启用的角色 | **PASS** — 5 条 |
| 5 | POST /role/update | 修改 test_code→test_role_updated | **PASS** — code=200 |
| 6 | POST /role/delete?id=xxx | 删除测试角色 | **PASS** — code=200 |
| 7 | POST /role/assignUserRole | userId=1, roleId=1 | **PASS** — code=200 |
| 8 | GET /role/userRole?userId=1 | 查询用户角色 | **PASS** — roleId=1 |

---

## 3. 权限接口测试结果（6 个接口）

| 编号 | 接口 | 测试 | 结果 |
|------|------|------|------|
| 1 | GET /permission/list | 分页查询 | **PASS** — code=200 |
| 2 | POST /permission/add | 新增 test:perm | **PASS** — code=200 |
| 3 | GET /permission/all | 所有权限 | **PASS** — 22 条（21初始化+1测试） |
| 4 | POST /permission/update | 修改test:perm | **PASS** — code=200 |
| 5 | GET /permission/rolePermissions?roleId=1 | super_admin权限 | **PASS** — 21 条 |
| 6 | POST /permission/delete?id=xxx | 删除测试权限 | **PASS** — code=200 |

---

## 4. JWT 验证

| 测试项 | 结果 |
|--------|------|
| /role/** 不带 token | 401 ✓ |
| /permission/** 不带 token | 401 ✓ |
| /role/** \+ /permission/** 不在白名单 | 确认未放行 |

---

## 5. 仍存在的问题

- DateFormatter 警告（不影响功能）
- 批量删除接口已实现但本次未单独测试
- assignRolePermissions 本次未直接调用 POST 测试（角色权限已通过 rolePermissions GET 验证）
