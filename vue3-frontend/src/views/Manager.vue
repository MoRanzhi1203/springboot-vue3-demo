<template>
  <div style="display: flex; flex-direction: column; height: 100vh">
    <!-- 顶部栏 -->
    <div style="height: 60px; background: #409eff; display: flex; align-items: center; justify-content: space-between; padding: 0 20px; color: #fff">
      <div style="font-size: 18px; font-weight: bold">
        双方碳智能监测与预测平台
      </div>
      <div style="display: flex; align-items: center; gap: 15px">
        <span>{{ loginUser?.name || loginUser?.username || '管理员' }}</span>
        <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
      </div>
    </div>

    <div style="display: flex; flex: 1; overflow: hidden">
      <!-- 左侧菜单 -->
      <div style="width: 220px; border-right: 1px solid #ddd; overflow-y: auto">
        <el-menu :default-active="route.path" router style="border: 0">
          <el-menu-item index="/Manager/Home">
            <el-icon><HomeFilled /></el-icon>
            <span>首页数据概览</span>
          </el-menu-item>

          <el-sub-menu index="monitor">
            <template #title>
              <el-icon><Monitor /></el-icon>
              <span>数据监测</span>
            </template>
            <el-menu-item index="/Manager/Monitor">数据监测面板</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="analysis">
            <template #title>
              <el-icon><TrendCharts /></el-icon>
              <span>碳排放分析</span>
            </template>
            <el-menu-item index="/Manager/Analysis">碳排放分析</el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/Manager/Emission">
            <el-icon><Aim /></el-icon>
            <span>减排管理</span>
          </el-menu-item>

          <el-sub-menu index="system">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统设置</span>
            </template>
            <el-menu-item index="/Manager/Data">用户管理</el-menu-item>
            <el-menu-item index="/Manager/Role">角色管理</el-menu-item>
            <el-menu-item index="/Manager/Permission">权限管理</el-menu-item>
            <el-menu-item index="/Manager/Log">日志管理</el-menu-item>
          </el-sub-menu>
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
import { HomeFilled, Monitor, TrendCharts, Aim, Setting } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 从本地存储读取登录用户信息
const loginUser = JSON.parse(localStorage.getItem('loginUser') || 'null')

const handleLogout = () => {
  localStorage.removeItem('loginUser')
  router.push('/login')
}
</script>

<style scoped>
</style>
