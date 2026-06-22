<template>
  <div>
    <!-- 搜索栏 -->
    <div>
      <el-input style="width: 300px; margin: 10px" placeholder="请输入姓名查询" v-model="data.name" :prefix-icon="Search" clearable @keyup.enter="searchData"></el-input>
      <el-button type="primary" @click="searchData">查询</el-button>
      <el-button type="success" @click="resetSearch">重置</el-button>
    </div>

    <!-- 操作按钮 -->
    <div style="margin-top: 5px">
      <el-button type="primary" @click="openAddDialog">新增</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="selectedIds.length === 0">批量删除</el-button>
      <el-button type="warning" @click="handleImport">导入</el-button>
      <el-button type="success" @click="handleExport">导出</el-button>
    </div>

    <!-- 数据表格 -->
    <div class="card" style="margin-top: 5px; margin-bottom: 5px">
      <el-table ref="tableRef" :data="data.tableData" border style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="账号" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="sex" label="性别" width="80" />
        <el-table-column prop="tel" label="电话" />
        <el-table-column prop="headurl" label="头像" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="openEditDialog(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="demo-pagination-block">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[5, 10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>

    <!-- 弹窗（新增/编辑）-->
    <div>
      <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '添加用户'" width="500px" @close="resetForm">
        <el-form :model="formData" label-width="60px">
          <el-form-item label="账号">
            <el-input v-model="formData.username" autocomplete="off" />
          </el-form-item>

          <el-form-item label="姓名">
            <el-input v-model="formData.name" autocomplete="off" />
          </el-form-item>

          <el-form-item label="性别">
            <el-select v-model="formData.sex" style="width: 100%">
              <el-option label="男" value="男" />
              <el-option label="女" value="女" />
            </el-select>
          </el-form-item>

          <el-form-item label="密码">
            <el-input v-model="formData.userpwd" autocomplete="off" show-password />
          </el-form-item>

          <el-form-item label="电话">
            <el-input v-model="formData.tel" autocomplete="off" />
          </el-form-item>

          <el-form-item label="头像">
            <el-input v-model="formData.headurl" autocomplete="off" />
          </el-form-item>
        </el-form>

        <template #footer>
          <div class="dialog-footer">
            <el-button @click="closeDialog">取消</el-button>
            <el-button type="primary" @click="confirmSave">确定</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { Search } from "@element-plus/icons-vue";
import { onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useAuthStore } from '@/stores/auth.js'

const authStore = useAuthStore()
// 表格组件引用
const tableRef = ref(null)

const data = reactive({
  name: '',
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// ==================== 分页查询 ====================
const fetchData = async () => {
  try {
    const params = new URLSearchParams({
      pagenum: data.pageNum,
      pagesize: data.pageSize,
      name: data.name || ''
    })
    const res = await fetch(`/api/admin/list?${params.toString()}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': authStore.getAuthorizationHeader()
      }
    })

    const result = await res.json()

    if (result.code === 200) {
      data.tableData = result.data.list
      data.total = result.data.total
    } else {
      console.error('获取数据失败', result.message)
    }
  } catch (error) {
    console.error('网络请求失败', error)
  }
}

const searchData = () => {
  data.pageNum = 1
  fetchData()
}

const resetSearch = () => {
  data.name = ''
  data.pageNum = 1
  fetchData()
}

const handleSizeChange = (val) => {
  data.pageSize = val
  data.pageNum = 1
  fetchData()
}

const handleCurrentChange = (val) => {
  data.pageNum = val
  fetchData()
}

// ==================== 表格多选 ====================
const selectedIds = ref([])

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// ==================== 新增 / 编辑弹窗 ====================
const dialogVisible = ref(false)
const isEdit = ref(false)

const formData = reactive({
  id: null,
  username: '',
  name: '',
  sex: '',
  userpwd: '',
  tel: '',
  headurl: ''
})

const resetForm = () => {
  formData.id = null
  formData.username = ''
  formData.name = ''
  formData.sex = ''
  formData.userpwd = ''
  formData.tel = ''
  formData.headurl = ''
  isEdit.value = false
}

const openAddDialog = () => {
  resetForm()
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  resetForm()
  isEdit.value = true
  formData.id = row.id
  formData.username = row.username
  formData.name = row.name
  formData.sex = row.sex
  formData.tel = row.tel
  formData.headurl = row.headurl
  dialogVisible.value = true
}

const closeDialog = () => {
  dialogVisible.value = false
}

const confirmSave = async () => {
  // 编辑时用 PUT / 新增时用 POST
  const url = isEdit.value ? '/api/admin/update' : '/api/admin/add'
  const method = isEdit.value ? 'PUT' : 'POST'

  try {
    const res = await fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': authStore.getAuthorizationHeader()
      },
      body: JSON.stringify({
        id: formData.id,
        username: formData.username,
        name: formData.name,
        sex: formData.sex,
        userpwd: formData.userpwd,
        tel: formData.tel,
        headurl: formData.headurl
      })
    })

    // 先检查响应状态
    if (!res.ok) {
      const text = await res.text()
      console.error(`请求失败 [${res.status}]:`, text)
      ElMessage.error(`服务器错误(${res.status})，请查看控制台`)
      return
    }

    const result = await res.json()

    if (result.code === 200) {
      ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
      closeDialog()
      fetchData()
    } else {
      ElMessage.error(result.message || '操作失败')
    }
  } catch (error) {
    console.error('网络请求失败：', error)
    ElMessage.error('网络连接失败，请确认后端服务已启动')
  }
}

// ==================== 删除 ====================
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await fetch(`/api/admin/delete/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': authStore.getAuthorizationHeader() }
      })
      const result = await res.json()

      if (result.code === 200) {
        ElMessage.success('删除成功')
        fetchData()
      } else {
        ElMessage.error(result.message || '删除失败')
      }
    } catch (error) {
      console.error('删除请求失败：', error)
      ElMessage.error('网络连接失败，请确认后端服务已启动')
    }
  }).catch(() => {})
}

// ==================== 批量删除 ====================
const handleBatchDelete = () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请至少选择一条数据')
    return
  }

  ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 条数据吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await fetch(`/api/admin/batchDelete?ids=${selectedIds.value.join(',')}`, {
        method: 'DELETE',
        headers: { 'Authorization': authStore.getAuthorizationHeader() }
      })
      const result = await res.json()

      if (result.code === 200) {
        ElMessage.success('批量删除成功')
        selectedIds.value = []
        // 清空表格选择状态
        if (tableRef.value) {
          tableRef.value.clearSelection()
        }
        fetchData()
      } else {
        ElMessage.error(result.message || '批量删除失败')
      }
    } catch (error) {
      console.error('批量删除请求失败：', error)
      ElMessage.error('网络连接失败，请确认后端服务已启动')
    }
  }).catch(() => {})
}

onMounted(() => {
  fetchData()
})

// ==================== 导入 / 导出（占位） ====================
const handleImport = () => {
  ElMessage.info('导入功能开发中，敬请期待')
}

const handleExport = () => {
  ElMessage.info('导出功能开发中，敬请期待')
}
</script>

<style scoped>
.demo-pagination-block {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
