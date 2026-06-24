<template>
  <div>
    <div>
      <el-input style="width: 160px; margin: 10px" placeholder="企业ID" v-model="search.companyId" clearable />
      <el-input style="width: 160px; margin: 10px" placeholder="年份" v-model="search.emissionYear" clearable />
      <el-button type="primary" @click="doSearch">查询</el-button>
      <el-button @click="doReset">重置</el-button>
    </div>
    <div style="margin-top: 5px"><el-button type="primary" @click="openAdd">新增</el-button></div>
    <el-table :data="tableData" border style="width:100%; margin-top:10px">
      <el-table-column prop="id" label="ID" width="60"/><el-table-column prop="companyId" label="企业ID" width="80"/>
      <el-table-column prop="emissionYear" label="年份" width="80"/><el-table-column prop="emissionMonth" label="月份" width="60"/>
      <el-table-column prop="scope1Emission" label="范围1" width="100"/><el-table-column prop="scope2Emission" label="范围2" width="100"/>
      <el-table-column prop="scope3Emission" label="范围3" width="100"/><el-table-column prop="totalEmission" label="总排放量" width="100"/>
      <el-table-column prop="verificationStatus" label="核查状态" width="100"/>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{row}"><el-button size="small" @click="openEdit(row)">编辑</el-button><el-button size="small" type="danger" @click="doDelete(row.id)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <div style="margin-top:20px;display:flex;justify-content:center"><el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[5,10,20,50]" layout="total,sizes,prev,pager,next" :total="total" @size-change="doSearch" @current-change="doSearch"/></div>
    <el-dialog v-model="dialogVisible" :title="isEdit?'编辑碳排放':'新增碳排放'" width="500px" @close="resetForm">
      <el-form :model="form" label-width="100px">
        <el-form-item label="企业ID"><el-input v-model="form.companyId"/></el-form-item>
        <el-form-item label="年份"><el-input v-model="form.emissionYear"/></el-form-item>
        <el-form-item label="月份"><el-input v-model="form.emissionMonth"/></el-form-item>
        <el-form-item label="范围1"><el-input v-model="form.scope1Emission"/></el-form-item>
        <el-form-item label="范围2"><el-input v-model="form.scope2Emission"/></el-form-item>
        <el-form-item label="范围3"><el-input v-model="form.scope3Emission"/></el-form-item>
        <el-form-item label="总排放量"><el-input v-model="form.totalEmission"/></el-form-item>
        <el-form-item label="核查状态"><el-input v-model="form.verificationStatus"/></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="doSave">确定</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import {ref,reactive,onMounted} from 'vue';import {ElMessage,ElMessageBox} from 'element-plus';import {useAuthStore} from '@/stores/auth.js'
const authStore=useAuthStore()
const tableData=ref([]),pageNum=ref(1),pageSize=ref(10),total=ref(0)
const search=reactive({companyId:'',emissionYear:''})
const dialogVisible=ref(false),isEdit=ref(false)
const form=reactive({id:null,companyId:'',emissionYear:'',emissionMonth:'',scope1Emission:'',scope2Emission:'',scope3Emission:'',totalEmission:'',verificationStatus:''})
const h=()=>({'Content-Type':'application/json','Authorization':authStore.getAuthorizationHeader()})
const fetchData=async()=>{const p=new URLSearchParams({pagenum:pageNum.value,pagesize:pageSize.value,companyId:search.companyId||'',emissionYear:search.emissionYear||''});const r=await fetch('/api/carbonEmission/list?'+p,{headers:h()}).then(r=>r.json());if(r.code===200){tableData.value=r.data.list;total.value=r.data.total}}
const doSearch=()=>{pageNum.value=1;fetchData()};const doReset=()=>{search.companyId='';search.emissionYear='';doSearch()}
const openAdd=()=>{resetForm();dialogVisible.value=true}
const openEdit=(row)=>{Object.assign(form,row);isEdit.value=true;dialogVisible.value=true}
const resetForm=()=>{Object.keys(form).forEach(k=>form[k]=k==='id'?null:'');isEdit.value=false}
const doSave=async()=>{const url=isEdit.value?'/api/carbonEmission/update':'/api/carbonEmission/add';const r=await fetch(url,{method:'POST',headers:h(),body:JSON.stringify(form)}).then(r=>r.json());if(r.code===200){ElMessage.success(isEdit.value?'修改成功':'新增成功');dialogVisible.value=false;fetchData()}else{ElMessage.error(r.message)}}
const doDelete=(id)=>{ElMessageBox.confirm('确定删除?','提示',{type:'warning'}).then(async()=>{const r=await fetch('/api/carbonEmission/delete?id='+id,{method:'POST',headers:h()}).then(r=>r.json());if(r.code===200){ElMessage.success('删除成功');fetchData()}else{ElMessage.error(r.message)}}).catch(()=>{})}
onMounted(fetchData)
</script>
