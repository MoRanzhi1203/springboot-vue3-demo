<template>
  <div style="display: flex; flex-direction: column; height: 100vh">
    <!-- 顶部栏 -->
    <div style="height: 60px; background: #409eff; display: flex; align-items: center; justify-content: space-between; padding: 0 20px; color: #fff">
      <div style="font-size: 18px; font-weight: bold">双方碳智能监测与预测平台</div>
      <div style="display: flex; align-items: center; gap: 15px">
        <span>{{ authStore.user?.name || authStore.user?.username || '未登录' }}</span>
        <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
      </div>
    </div>

    <div style="display: flex; flex: 1; overflow: hidden">
      <!-- 左侧菜单 -->
      <div style="width: 220px; border-right: 1px solid #ddd; overflow-y: auto">
        <el-menu :default-active="route.path" router style="border: 0">
          <el-menu-item index="/Manager/Dashboard">
            <el-icon><HomeFilled /></el-icon><span>系统首页</span>
          </el-menu-item>
          <el-menu-item index="/Manager/Data">
            <el-icon><User /></el-icon><span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/Manager/Company">
            <el-icon><OfficeBuilding /></el-icon><span>企业账户管理</span>
          </el-menu-item>
          <el-menu-item index="/Manager/CarbonEmission">
            <el-icon><TrendCharts /></el-icon><span>碳排放管理</span>
          </el-menu-item>
          <el-menu-item index="/Manager/CarbonTrade">
            <el-icon><Coin /></el-icon><span>碳交易管理</span>
          </el-menu-item>
          <el-menu-item index="/Manager/NewEnergyProject">
            <el-icon><Sunny /></el-icon><span>新能源项目管理</span>
          </el-menu-item>
          <el-menu-item index="/Manager/EnergySavingGoal">
            <el-icon><Aim /></el-icon><span>节能减排目标管理</span>
          </el-menu-item>
          <el-menu-item index="/Manager/Role">
            <el-icon><Key /></el-icon><span>角色管理</span>
          </el-menu-item>
          <el-menu-item index="/Manager/Permission">
            <el-icon><Lock /></el-icon><span>权限管理</span>
          </el-menu-item>
        </el-menu>
      </div>

      <!-- 右侧内容区域 -->
      <div style="flex: 1; padding: 20px; overflow-y: auto; background: #f5f7fa">
        <RouterView />
      </div>
    </div>
  </div>
</template>

<script setup>
import { RouterView } from 'vue-router'
import { useRouter, useRoute } from 'vue-router'
import { HomeFilled, User, OfficeBuilding, TrendCharts, Coin, Sunny, Aim, Key, Lock } from '@element-plus/icons-vue'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const handleLogout = () => {
  authStore.logout()
  router.push('/Login')
}
</script>
