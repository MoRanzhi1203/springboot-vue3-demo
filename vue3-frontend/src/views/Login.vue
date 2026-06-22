<template>
  <div class="login-container">
    <div class="login-card">
      <h2 class="login-title">双方碳智能监测与预测平台</h2>
      <h3 class="login-subtitle">用户登录</h3>

      <el-form :model="formData" label-width="0" size="large">
        <el-form-item>
          <el-input v-model="formData.username" placeholder="请输入账号" :prefix-icon="User" />
        </el-form-item>

        <el-form-item>
          <el-input v-model="formData.userpwd" type="password" placeholder="请输入密码" :prefix-icon="Lock" show-password />
        </el-form-item>

        <el-form-item>
          <div style="display: flex; gap: 10px">
            <el-input v-model="formData.code" placeholder="验证码" :prefix-icon="Key" style="flex: 1" />
            <ValidCode ref="validCodeRef" v-model="validCode" />
          </div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="handleLogin" :loading="loading">登 录</el-button>
        </el-form-item>

        <div style="text-align: center">
          还没有账号？<el-link type="primary" @click="router.push('/register')">立即注册</el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Key } from '@element-plus/icons-vue'
import ValidCode from '../components/ValidCode.vue'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)
// 验证码组件引用（用于刷新验证码）
const validCodeRef = ref(null)
// 验证码 v-model
const validCode = ref('')

const formData = reactive({
  username: '',
  userpwd: '',
  code: ''
})

const handleLogin = async () => {
  // 参数校验
  if (!formData.username) {
    ElMessage.warning('请输入账号')
    refreshCaptcha()
    return
  }
  if (!formData.userpwd) {
    ElMessage.warning('请输入密码')
    refreshCaptcha()
    return
  }
  if (!formData.code) {
    ElMessage.warning('请输入验证码')
    refreshCaptcha()
    return
  }

  // 验证码校验（不区分大小写）
  if (formData.code.toLowerCase() !== validCode.value.toLowerCase()) {
    ElMessage.error('验证码错误')
    refreshCaptcha()
    return
  }

  loading.value = true
  try {
    const res = await fetch('/api/admin/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: formData.username,
        userpwd: formData.userpwd
      })
    })
    const result = await res.json()

    if (result.code === 200) {
      ElMessage.success('登录成功')
      // 使用 Pinia authStore 保存登录状态
      authStore.login(result.data)
      router.push('/Manager/Home')
    } else {
      ElMessage.error(result.message || '用户名或密码错误')
      refreshCaptcha()
    }
  } catch (error) {
    console.error('登录请求失败：', error)
    ElMessage.error('网络连接失败，请确认后端服务已启动')
    refreshCaptcha()
  } finally {
    loading.value = false
  }
}

// 刷新验证码
const refreshCaptcha = () => {
  formData.code = ''
  if (validCodeRef.value) {
    validCodeRef.value.refreshCode()
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 420px;
  padding: 40px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.login-title {
  text-align: center;
  font-size: 22px;
  color: #303133;
  margin-bottom: 8px;
}

.login-subtitle {
  text-align: center;
  font-size: 16px;
  color: #909399;
  margin-bottom: 30px;
  font-weight: normal;
}
</style>
