<template>
  <div>
    <div><el-input style="width:200px;margin:10px" placeholder="交易编号" v-model="search.tradeNo" clearable/><el-button type="primary" @click="doSearch">查询</el-button><el-button @click="doReset">重置</el-button></div>
    <div style="margin-top:5px"><el-button type="primary" @click="openAdd">新增</el-button></div>
    <el-table :data="tableData" border style="width:100%;margin-top:10px">
      <el-table-column prop="id" label="ID" width="60"/><el-table-column prop="tradeNo" label="交易编号" width="140"/>
      <el-table-column prop="sellerCompanyId" label="卖方企业ID" width="100"/><el-table-column prop="buyerCompanyId" label="买方企业ID" width="100"/>
      <el-table-column prop="tradeType" label="交易类型" width="100"/><el-table-column prop="tradeQuantity" label="交易数量" width="100"/>
      <el-table-column prop="tradePrice" label="交易价格" width="100"/><el-table-column prop="tradeDate" label="交易日期" width="120"/>
      <el-table-column prop="status" label="状态" width="100"/>
      <el-table-column label="操作" width="150" fixed="right"><template #default="{row}"><el-button size="small" @click="openEdit(row)">编辑</el-button><el-button size="small" type="danger" @click="doDelete(row.id)">删除</el-button></template></el-table-column>
    </el-table>
    <div style="margin-top:20px;display:flex;justify-content:center"><el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[5,10,20,50]" layout="total,sizes,prev,pager,next" :total="total" @size-change="doSearch" @current-change="doSearch"/></div>
    <el-dialog v-model="dialogVisible" :title="isEdit?'编辑碳交易':'新增碳交易'" width="500px" @close="resetForm">
      <el-form :model="form" label-width="100px">
        <el-form-item label="交易编号"><el-input v-model="form.tradeNo"/></el-form-item>
        <el-form-item label="卖方企业ID"><el-input v-model="form.sellerCompanyId"/></el-form-item>
        <el-form-item label="买方企业ID"><el-input v-model="form.buyerCompanyId"/></el-form-item>
        <el-form-item label="交易类型"><el-input v-model="form.tradeType"/></el-form-item>
        <el-form-item label="交易数量"><el-input v-model="form.tradeQuantity"/></el-form-item>
        <el-form-item label="交易价格"><el-input v-model="form.tradePrice"/></el-form-item>
        <el-form-item label="交易日期"><el-input v-model="form.tradeDate"/></el-form-item>
        <el-form-item label="状态"><el-input v-model="form.status"/></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="doSave">确定</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import {ref,reactive,onMounted} from 'vue';import {ElMessage,ElMessageBox} from 'element-plus';import {useAuthStore} from '@/stores/auth.js'
const authStore=useAuthStore()
const tableData=ref([]),pageNum=ref(1),pageSize=ref(10),total=ref(0),search=reactive({tradeNo:''})
const dialogVisible=ref(false),isEdit=ref(false)
const form=reactive({id:null,tradeNo:'',sellerCompanyId:'',buyerCompanyId:'',tradeType:'',tradeQuantity:'',tradePrice:'',tradeDate:'',status:''})
const h=()=>({'Content-Type':'application/json','Authorization':authStore.getAuthorizationHeader()})
const fetchData=async()=>{const p=new URLSearchParams({pagenum:pageNum.value,pagesize:pageSize.value,tradeNo:search.tradeNo||''});const r=await fetch('/api/carbonTrade/list?'+p,{headers:h()}).then(r=>r.json());if(r.code===200){tableData.value=r.data.list;total.value=r.data.total}}
const doSearch=()=>{pageNum.value=1;fetchData()};const doReset=()=>{search.tradeNo='';doSearch()}
const openAdd=()=>{resetForm();dialogVisible.value=true}
const openEdit=(row)=>{Object.assign(form,row);isEdit.value=true;dialogVisible.value=true}
const resetForm=()=>{Object.keys(form).forEach(k=>form[k]=k==='id'?null:'');isEdit.value=false}
const doSave=async()=>{const url=isEdit.value?'/api/carbonTrade/update':'/api/carbonTrade/add';const r=await fetch(url,{method:'POST',headers:h(),body:JSON.stringify(form)}).then(r=>r.json());if(r.code===200){ElMessage.success(isEdit.value?'修改成功':'新增成功');dialogVisible.value=false;fetchData()}else{ElMessage.error(r.message)}}
const doDelete=(id)=>{ElMessageBox.confirm('确定删除？','提示',{type:'warning'}).then(async()=>{const r=await fetch('/api/carbonTrade/delete?id='+id,{method:'POST',headers:h()}).then(r=>r.json());if(r.code===200){ElMessage.success('删除成功');fetchData()}else{ElMessage.error(r.message)}}).catch(()=>{})}
onMounted(fetchData)
</script>
