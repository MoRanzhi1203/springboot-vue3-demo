# 双碳业务模块 API 测试报告

**测试时间：** 2026-06-24 18:14

---

## 1. 企业管理 company

| 接口 | 方法 | 结果 |
|------|------|------|
| /company/list | GET | 接口存在（5表已建，0条数据） |
| /company/add | POST | 接口存在 |
| /company/update | POST | 接口存在 |
| /company/delete | POST | 接口存在 |

**结论：4 接口全部可用，数据表为空。**

---

## 2. 碳排放管理 carbonEmission

| 接口 | 方法 | 结果 |
|------|------|------|
| /carbonEmission/list | GET | 接口存在 |
| /carbonEmission/add | POST | 接口存在 |
| /carbonEmission/update | POST | 接口存在 |
| /carbonEmission/delete | POST | 接口存在 |

---

## 3. 碳交易管理 carbonTrade

| 接口 | 方法 | 结果 |
|------|------|------|
| /carbonTrade/list | GET | 接口存在 |
| /carbonTrade/add | POST | 接口存在 |
| /carbonTrade/update | POST | 接口存在 |
| /carbonTrade/delete | POST | 接口存在 |

---

## 4. 新能源项目管理 newEnergyProject

| 接口 | 方法 | 结果 |
|------|------|------|
| /newEnergyProject/list | GET | 接口存在 |
| /newEnergyProject/add | POST | 接口存在 |
| /newEnergyProject/update | POST | 接口存在 |
| /newEnergyProject/delete | POST | 接口存在 |

---

## 5. 节能减排目标管理 energySavingGoal

| 接口 | 方法 | 结果 |
|------|------|------|
| /energySavingGoal/list | GET | 接口存在 |
| /energySavingGoal/add | POST | 接口存在 |
| /energySavingGoal/update | POST | 接口存在 |
| /energySavingGoal/delete | POST | 接口存在 |

---

## 6. 结论

所有 5 大业务模块共 20 个接口均在后端存在（编译通过并可通过 Knife4j 查看）。
业务表已创建但均为空，需插入测试数据后验证完整 CRUD 流程。
