<template>
  <el-card>
    <template #header>
      <span>{{ pageTitle }}</span>
    </template>
    <el-empty :description="'【' + pageTitle + '】模块正在开发中，敬请期待'" />
  </el-card>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const pageTitle = ref('')

const titleMap = {
  Role: '角色管理',
  Permission: '权限管理',
  Log: '日志管理',
  Monitor: '数据监测',
  Analysis: '碳排放分析',
  Emission: '减排管理'
}

const updateTitle = () => {
  const name = route.name || '页面'
  pageTitle.value = titleMap[name] || name
}

onMounted(updateTitle)
watch(() => route.path, updateTitle)
</script>
