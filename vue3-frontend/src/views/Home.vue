<template>
  <div class="home-container">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span class="welcome-text">欢迎使用后台管理系统</span>
        </div>
      </template>
      <div class="welcome-content">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-statistic title="用户总数" :value="userCount">
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="数据总量" :value="dataCount">
              <template #prefix>
                <el-icon><Document /></el-icon>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="今日访问" :value="visitCount">
              <template #prefix>
                <el-icon><View /></el-icon>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="系统消息" :value="messageCount">
              <template #prefix>
                <el-icon><Bell /></el-icon>
              </template>
            </el-statistic>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>快捷操作</span>
            </div>
          </template>
          <div class="quick-actions">
            <el-button type="primary" @click="goToData">数据管理</el-button>
            <el-button type="success" @click="goToUsers">用户管理</el-button>
            <el-button type="warning" @click="viewStats">查看统计</el-button>
            <el-button type="info" @click="openSettings">系统设置</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>系统信息</span>
            </div>
          </template>
          <div class="system-info">
            <p><strong>系统版本：</strong>v1.0.0</p >
            <p><strong>技术栈：</strong>Spring Boot + Vue 3 + Element Plus</p >
            <p><strong>当前时间：</strong>{{ currentTime }}</p >
            <p><strong>运行状态：</strong><el-tag type="success">正常运行</el-tag></p >
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { User, Document, View, Bell } from '@element-plus/icons-vue'

const router = useRouter()

const userCount = ref(1234)
const dataCount = ref(5678)
const visitCount = ref(910)
const messageCount = ref(42)

const currentTime = ref('')

const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

const goToData = () => {
  router.push('/Manager/Data')
}

const goToUsers = () => {
  router.push('/Manager/Data')
}

const viewStats = () => {
  router.push('/Manager/Home')
}

const openSettings = () => {
  router.push('/Manager/Home')
}

onMounted(() => {
  updateTime()
  setInterval(updateTime, 1000)
})
</script>

<style scoped>
.home-container {
  padding: 20px;
}

.welcome-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-text {
  font-size: 18px;
  font-weight: bold;
}

.welcome-content {
  padding: 20px 0;
}

.quick-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.system-info p {
  margin: 10px 0;
  line-height: 1.6;
}
</style>
