<template>
  <div class="register-container">
    <div class="register-card">
      <h2 class="register-title">双方碳智能监测与预测平台</h2>
      <h3 class="register-subtitle">用户注册</h3>

      <el-form :model="formData" label-width="0" size="large">
        <el-form-item>
          <el-input v-model="formData.username" placeholder="请输入账号" :prefix-icon="User" />
        </el-form-item>

        <el-form-item>
          <el-input v-model="formData.userpwd" type="password" placeholder="请输入密码" :prefix-icon="Lock" show-password />
        </el-form-item>

        <el-form-item>
          <el-input v-model="formData.name" placeholder="请输入姓名" :prefix-icon="Avatar" />
        </el-form-item>

        <el-form-item>
          <el-select v-model="formData.sex" placeholder="请选择性别" style="width: 100%">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-input v-model="formData.tel" placeholder="请输入电话" :prefix-icon="Phone" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="handleRegister" :loading="loading">注 册</el-button>
        </el-form-item>

        <div style="text-align: center">
          已有账号？<el-link type="primary" @click="router.push('/Login')">立即登录</el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Avatar, Phone } from '@element-plus/icons-vue'

const router = useRouter()
const loading = ref(false)

const formData = reactive({
  username: '',
  userpwd: '',
  name: '',
  sex: '',
  tel: ''
})

const handleRegister = async () => {
  // 参数校验
  if (!formData.username) {
    ElMessage.warning('请输入账号')
    return
  }
  if (!formData.userpwd) {
    ElMessage.warning('请输入密码')
    return
  }
  if (!formData.name) {
    ElMessage.warning('请输入姓名')
    return
  }
  if (!formData.sex) {
    ElMessage.warning('请选择性别')
    return
  }
  if (!formData.tel) {
    ElMessage.warning('请输入电话')
    return
  }

  loading.value = true
  try {
    const res = await fetch('/api/admin/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: formData.username,
        userpwd: formData.userpwd,
        name: formData.name,
        sex: formData.sex,
        tel: formData.tel,
        headurl: ''
      })
    })
    const result = await res.json()

    if (result.code === 200) {
      ElMessage.success('注册成功，请登录')
      router.push('/Login')
    } else {
      ElMessage.error(result.message || '注册失败')
    }
  } catch (error) {
    console.error('注册请求失败：', error)
    ElMessage.error('网络连接失败，请确认后端服务已启动')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.register-card {
  width: 420px;
  padding: 40px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.register-title {
  text-align: center;
  font-size: 22px;
  color: #303133;
  margin-bottom: 8px;
}

.register-subtitle {
  text-align: center;
  font-size: 16px;
  color: #909399;
  margin-bottom: 30px;
  font-weight: normal;
}
</style>
