<template>
  <div>
    <div>
      <el-input style="width: 300px; margin: 10px" placeholder="请输入企业名称" v-model="search.name" clearable @keyup.enter="doSearch"></el-input>
      <el-button type="primary" @click="doSearch">查询</el-button>
      <el-button @click="doReset">重置</el-button>
    </div>
    <div style="margin-top: 5px">
      <el-button type="primary" @click="openAdd">新增</el-button>
    </div>
    <el-table :data="tableData" border style="width: 100%; margin-top: 10px">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="companyName" label="企业名称" />
      <el-table-column prop="companyCode" label="企业编码" width="120" />
      <el-table-column prop="industry" label="行业" width="100" />
      <el-table-column prop="contactPerson" label="联系人" width="100" />
      <el-table-column prop="contactPhone" label="联系电话" width="120" />
      <el-table-column prop="emissionQuota" label="碳排放配额" width="120" />
      <el-table-column prop="status" label="状态" width="80" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{row}">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="doDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 20px; display: flex; justify-content: center">
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[5,10,20,50]" layout="total,sizes,prev,pager,next" :total="total" @size-change="doSearch" @current-change="doSearch" />
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit?'编辑企业':'新增企业'" width="500px" @close="resetForm">
      <el-form :model="form" label-width="100px">
        <el-form-item label="企业名称"><el-input v-model="form.companyName" /></el-form-item>
        <el-form-item label="企业编码"><el-input v-model="form.companyCode" /></el-form-item>
        <el-form-item label="行业"><el-input v-model="form.industry" /></el-form-item>
        <el-form-item label="联系人"><el-input v-model="form.contactPerson" /></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="form.contactPhone" /></el-form-item>
        <el-form-item label="碳排放配额"><el-input v-model="form.emissionQuota" /></el-form-item>
        <el-form-item label="状态"><el-input v-model="form.status" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="doSave">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth.js'
const authStore = useAuthStore()

const tableData = ref([])
const pageNum = ref(1), pageSize = ref(10), total = ref(0)
const search = reactive({ name: '' })
const dialogVisible = ref(false), isEdit = ref(false)
const form = reactive({ id: null, companyName: '', companyCode: '', industry: '', contactPerson: '', contactPhone: '', emissionQuota: '', status: '' })

const headers = () => ({ 'Content-Type': 'application/json', 'Authorization': authStore.getAuthorizationHeader() })

const fetchData = async () => {
  const p = new URLSearchParams({ pagenum: pageNum.value, pagesize: pageSize.value, companyName: search.name || '' })
  const r = await fetch('/api/company/list?'+p, { headers: headers() }).then(res => res.json())
  if (r.code === 200) { tableData.value = r.data.list; total.value = r.data.total }
}
const doSearch = () => { pageNum.value = 1; fetchData() }
const doReset = () => { search.name = ''; doSearch() }
const openAdd = () => { resetForm(); dialogVisible.value = true }
const openEdit = (row) => { Object.assign(form, row); isEdit.value = true; dialogVisible.value = true }
const resetForm = () => { Object.keys(form).forEach(k => form[k] = k==='id'?null:''); isEdit.value = false }
const doSave = async () => {
  const url = isEdit.value ? '/api/company/update' : '/api/company/add'
  const r = await fetch(url, { method: 'POST', headers: headers(), body: JSON.stringify(form) }).then(res => res.json())
  if (r.code === 200) { ElMessage.success(isEdit.value?'修改成功':'新增成功'); dialogVisible.value = false; fetchData() } else { ElMessage.error(r.message) }
}
const doDelete = (id) => {
  ElMessageBox.confirm('确定删除？','提示',{type:'warning'}).then(async () => {
    const r = await fetch('/api/company/delete?id='+id, { method: 'POST', headers: headers() }).then(res => res.json())
    if (r.code === 200) { ElMessage.success('删除成功'); fetchData() } else { ElMessage.error(r.message) }
  }).catch(() => {})
}
onMounted(fetchData)
</script>
