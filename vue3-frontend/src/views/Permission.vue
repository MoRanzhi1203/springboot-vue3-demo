<template>
  <div>
    <div><el-input style="width:200px;margin:10px" placeholder="权限名称" v-model="search.name" clearable/><el-input style="width:200px;margin:10px" placeholder="权限编码" v-model="search.code" clearable/><el-button type="primary" @click="doSearch">查询</el-button><el-button @click="doReset">重置</el-button></div>
    <div style="margin-top:5px"><el-button type="primary" @click="openAdd">新增</el-button><el-button type="danger" @click="handleBatchDelete" :disabled="selectedIds.length===0">批量删除</el-button></div>
    <el-table :data="tableData" border style="width:100%;margin-top:10px" @selection-change="handleSelect" ref="tableRef">
      <el-table-column type="selection" width="50"/><el-table-column prop="permissionName" label="权限名称"/><el-table-column prop="permissionCode" label="权限编码"/>
      <el-table-column prop="module" label="所属模块" width="120"/><el-table-column prop="permissionType" label="权限类型" width="80"/><el-table-column prop="path" label="路径"/>
      <el-table-column prop="sortOrder" label="排序" width="60"/><el-table-column prop="status" label="状态" width="60"/>
      <el-table-column label="操作" width="150" fixed="right"><template #default="{row}"><el-button size="small" @click="openEdit(row)">编辑</el-button><el-button size="small" type="danger" @click="doDelete(row.id)">删除</el-button></template></el-table-column>
    </el-table>
    <div style="margin-top:20px;display:flex;justify-content:center"><el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[5,10,20]" layout="total,sizes,prev,pager,next" :total="total" @size-change="doSearch" @current-change="doSearch"/></div>
    <el-dialog v-model="dialogVisible" :title="isEdit?'编辑权限':'新增权限'" width="500px" @close="resetForm"><el-form :model="form" label-width="80px"><el-form-item label="权限名称"><el-input v-model="form.permissionName"/></el-form-item><el-form-item label="权限编码"><el-input v-model="form.permissionCode"/></el-form-item><el-form-item label="所属模块"><el-input v-model="form.module"/></el-form-item><el-form-item label="权限类型"><el-input v-model="form.permissionType"/></el-form-item><el-form-item label="路径"><el-input v-model="form.path"/></el-form-item><el-form-item label="排序"><el-input v-model="form.sortOrder"/></el-form-item><el-form-item label="状态"><el-select v-model="form.status" style="width:100%"><el-option label="启用" :value="1"/><el-option label="禁用" :value="0"/></el-select></el-form-item></el-form><template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="doSave">确定</el-button></template></el-dialog>
  </div>
</template>
<script setup>
import {ref,reactive,onMounted} from 'vue';import {ElMessage,ElMessageBox} from 'element-plus';import {useAuthStore} from '@/stores/auth.js'
const authStore=useAuthStore();const tableRef=ref(null)
const tableData=ref([]),pageNum=ref(1),pageSize=ref(10),total=ref(0),selectedIds=ref([]),search=reactive({name:'',code:''})
const dialogVisible=ref(false),isEdit=ref(false),form=reactive({id:null,permissionName:'',permissionCode:'',module:'',permissionType:'button',path:'',sortOrder:'',status:1})
const h=()=>({'Content-Type':'application/json','Authorization':authStore.getAuthorizationHeader()})
const fetchData=async()=>{const p=new URLSearchParams({pagenum:pageNum.value,pagesize:pageSize.value,permissionName:search.name||'',permissionCode:search.code||''});const r=await fetch('/api/permission/list?'+p,{headers:h()}).then(r=>r.json());if(r.code===200){tableData.value=r.data.list;total.value=r.data.total}}
const doSearch=()=>{pageNum.value=1;fetchData()};const doReset=()=>{search.name='';search.code='';doSearch()}
const handleSelect=(s)=>{selectedIds.value=s.map(i=>i.id)}
const openAdd=()=>{resetForm();dialogVisible.value=true}
const openEdit=(row)=>{Object.assign(form,row);isEdit.value=true;dialogVisible.value=true}
const resetForm=()=>{form.id=null;form.permissionName='';form.permissionCode='';form.module='';form.permissionType='button';form.path='';form.sortOrder='';form.status=1;isEdit.value=false}
const doSave=async()=>{const url=isEdit.value?'/api/permission/update':'/api/permission/add';const r=await fetch(url,{method:'POST',headers:h(),body:JSON.stringify(form)}).then(r=>r.json());if(r.code===200){ElMessage.success(isEdit.value?'修改成功':'新增成功');dialogVisible.value=false;fetchData()}else{ElMessage.error(r.message)}}
const doDelete=(id)=>{ElMessageBox.confirm('确定删除?','提示',{type:'warning'}).then(async()=>{const r=await fetch('/api/permission/delete?id='+id,{method:'POST',headers:h()}).then(r=>r.json());if(r.code===200){ElMessage.success('删除成功');fetchData()}else{ElMessage.error(r.message)}}).catch(()=>{})}
const handleBatchDelete=()=>{if(!selectedIds.value.length)return ElMessage.warning('请选择要删除的权限');ElMessageBox.confirm('确定删除选中权限?','提示',{type:'warning'}).then(async()=>{const r=await fetch('/api/permission/batchDelete',{method:'POST',headers:h(),body:JSON.stringify(selectedIds.value)}).then(r=>r.json());if(r.code===200){ElMessage.success('批量删除成功');selectedIds.value=[];tableRef.value.clearSelection();fetchData()}else{ElMessage.error(r.message)}}).catch(()=>{})}
onMounted(fetchData)
</script>
