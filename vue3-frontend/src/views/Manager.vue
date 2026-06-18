<template>
  <div style="display: flex">
    <!-- 左侧部分-->
    <div style="width: 200px;border-right: 1px solid #ddd;min-height: calc(100vh - 60px)">

      <el-menu router :default-active="router.currentRoute.value.path" style="border: 0">
        <el-menu-item index="/Manager/Home">系统首页</el-menu-item>

        <el-sub-menu index="1">
          <template #title>
            <el-icon><location /></el-icon>
            <span>用户管理</span>
          </template>

          <el-menu-item-group>
            <el-menu-item index="/Manager/Data">普通用户管理</el-menu-item>
            <el-menu-item index="/Manager/Data">管理员管理</el-menu-item>
          </el-menu-item-group>
        </el-sub-menu>
      </el-menu>

    </div>

    <!-- 右侧内容区域 -->
    <div style="flex: 1; padding: 20px">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Location } from '@element-plus/icons-vue'

const router = useRouter()
</script>

<style scoped>
</style>