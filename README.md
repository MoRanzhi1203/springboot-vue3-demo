# 双方碳智能监测与预测平台

基于 Spring Boot 3 + Vue 3 的全栈碳监测管理平台，包含用户管理、数据仪表盘、碳排放分析和减排管理等功能模块。

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| JDK | 17+ | Java 开发环境 |
| Spring Boot | 3.5.15 | 核心框架 |
| MyBatis-Plus | 3.5.15 | ORM 框架 |
| MySQL | 8.x | 关系型数据库 |
| Druid | 1.2.23 | 数据库连接池 |
| PageHelper | 2.1.0 | 分页插件 |
| Knife4j | 4.4.0 | API 接口文档 |
| Lombok | — | 代码简化 |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5.x | 前端框架 |
| Vite | 8.x | 构建工具 |
| Element Plus | 2.14.x | UI 组件库 |
| Vue Router | 5.x | 路由管理 |
| Pinia | 3.x | 状态管理 |
| ECharts | 5.x | 数据可视化 |

---

## 项目结构

```
springboot-vue3-demo/
├── springboot-demo-backend/         # 后端 Spring Boot 项目
│   ├── src/main/java/com/example/springbootdemobackend/
│   │   ├── config/
│   │   │   ├── CorsConfig.java          # 跨域配置
│   │   │   └── Knife4jConfig.java       # 接口文档配置
│   │   ├── controller/
│   │   │   ├── AdminController.java     # 用户管理接口
│   │   │   └── TestController.java      # 测试接口
│   │   ├── entity/
│   │   │   └── Admin.java               # 用户实体
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java  # 全局异常处理
│   │   ├── mapper/
│   │   │   └── AdminMapper.java         # 用户 Mapper
│   │   ├── response/
│   │   │   ├── R.java                   # 统一响应类
│   │   │   └── ResponseCode.java        # 响应状态枚举
│   │   └── service/
│   │       ├── AdminService.java        # 用户服务接口
│   │       └── impl/
│   │           └── AdminServiceImpl.java    # 用户服务实现
│   └── src/main/resources/
│       ├── application.yml              # 应用配置
│       └── db/
│           └── init.sql                 # 数据库初始化脚本
│
├── vue3-frontend/                    # 前端 Vue3 项目
│   ├── src/
│   │   ├── components/
│   │   │   └── ValidCode.vue            # Canvas 验证码组件
│   │   ├── router/
│   │   │   └── index.js                 # 路由配置 + 守卫
│   │   ├── stores/
│   │   │   └── auth.js                  # Pinia 认证状态管理
│   │   ├── views/
│   │   │   ├── Login.vue                # 登录页面
│   │   │   ├── Register.vue             # 注册页面
│   │   │   ├── Manager.vue              # 后台管理布局
│   │   │   ├── Home.vue                 # 仪表盘首页
│   │   │   ├── Data.vue                 # 用户管理页
│   │   │   ├── Monitor.vue              # 数据监测面板
│   │   │   ├── Analysis.vue             # 碳排放分析页
│   │   │   ├── Emission.vue             # 减排管理页
│   │   │   └── Placeholder.vue          # 占位组件
│   │   ├── App.vue
│   │   └── main.js
│   ├── package.json
│   └── vite.config.js
└── .gitignore
```

---

## 功能模块

### 已完成

| 模块 | 后端 | 前端 | 说明 |
|------|------|------|------|
| 用户管理 CRUD | 增删改查 + 批量删除 | 表格 / 分页 / 弹窗 | 完整闭环 |
| 用户登录 | POST /admin/login | 账号 + 密码 + 验证码 | Pinia 状态保持 |
| 用户注册 | POST /admin/register | 账号 / 密码 / 姓名 / 电话 | 用户名重复校验 |
| 首页仪表盘 | — | ECharts 趋势图 / 占比图 | 模拟数据 |
| 数据监测面板 | — | 柱状图 / 折线图 | 模拟数据 |
| 碳排放分析 | — | Top10 排行 / 行业筛选 | 模拟数据 |
| 减排管理 | — | 进度条 / 企业排行 | 模拟数据 |
| 路由守卫 | — | 白名单 / 未登录拦截 | Pinia restoreSession |
| 接口文档 | Knife4j | — | http://localhost:8080/doc.html |
| Druid 监控 | — | — | http://localhost:8080/druid |

### 待开发

- 角色 / 权限 / 日志管理（前端占位已创建）
- 后端碳数据表设计与 API 接口
- 数据导入导出（Excel）
- JWT Token 认证
- 密码 BCrypt 加密

---

## 快速开始

### 环境要求

- JDK 17+
- Node.js 20+
- MySQL 8.0+
- Maven 3.6+

### 1. 数据库准备

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS `tanku` DEFAULT CHARSET=utf8mb4;
```

启动后端时 SQL 脚本会自动执行（`spring.sql.init.mode=always`），无需手动导入。

如果数据库已有 admin 表但缺少 deleted 列，手动执行：

```sql
ALTER TABLE `admin` ADD COLUMN `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除';
```

### 2. 修改数据库连接

编辑 `springboot-demo-backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tanku?...
    username: root
    password: 你的密码
```

### 3. 启动后端

```bash
cd springboot-demo-backend
mvn spring-boot:run
# 后端运行在 http://localhost:8080
```

### 4. 启动前端

```bash
cd vue3-frontend
npm install
npm run dev
# 前端运行在 http://localhost:5173
```

### 5. 访问页面

| 地址 | 说明 |
|------|------|
| http://localhost:5173 | 前端首页（自动跳转登录） |
| http://localhost:8080/doc.html | Knife4j 接口文档 |
| http://localhost:8080/druid | Druid 监控面板 (admin/admin) |

### 6. 测试账号

| 账号 | 密码 |
|------|------|
| zhangsan | 123456 |
| lisi | 123456 |

---

## API 接口

所有接口统一返回格式：

```json
{
  "code": 200,
  "message": "成功",
  "data": {}
}
```

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /admin/list?pagenum=1&pagesize=10&name= | 分页查询用户 |
| POST | /admin/add | 新增用户 |
| PUT | /admin/update | 编辑用户 |
| DELETE | /admin/delete/{id} | 删除用户 |
| DELETE | /admin/batchDelete?ids=1,2,3 | 批量删除 |
| POST | /admin/login | 用户登录 |
| POST | /admin/register | 用户注册 |

---

## 前端路由

| 路径 | 页面 | 权限 |
|------|------|------|
| /login | 登录页 | 公开 |
| /register | 注册页 | 公开 |
| /Manager/Home | 首页仪表盘 | 需登录 |
| /Manager/Data | 用户管理 | 需登录 |
| /Manager/Monitor | 数据监测 | 需登录 |
| /Manager/Analysis | 碳排放分析 | 需登录 |
| /Manager/Emission | 减排管理 | 需登录 |
| /Manager/Role | 角色管理（占位） | 需登录 |
| /Manager/Permission | 权限管理（占位） | 需登录 |
| /Manager/Log | 日志管理（占位） | 需登录 |

---

## 前端代理配置

Vite 开发服务器将 `/api` 前缀请求代理到后端：

```
/api/admin/list → http://localhost:8080/admin/list
```

---

## 数据库表

### admin 用户表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键（自增） |
| username | VARCHAR(50) | 账号 |
| userpwd | VARCHAR(100) | 密码 |
| name | VARCHAR(50) | 姓名 |
| sex | VARCHAR(10) | 性别 |
| tel | VARCHAR(20) | 电话 |
| headurl | VARCHAR(255) | 头像地址 |
| deleted | TINYINT | 逻辑删除（0=未删除，1=已删除） |

---

## 许可证

本项目用于教学实训目的。
