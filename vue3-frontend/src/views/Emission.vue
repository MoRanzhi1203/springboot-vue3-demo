<template>
  <div>
    <h1 style="margin-bottom: 20px">减排管理</h1>

    <!-- 指标卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <el-statistic title="年度减排目标(万吨)" :value="500">
            <template #prefix><el-icon color="#409eff"><Aim /></el-icon></template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <el-statistic title="已完成减排(万吨)" :value="312">
            <template #prefix><el-icon color="#67c23a"><CircleCheckFilled /></el-icon></template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <el-statistic title="完成率" :value="62.4" suffix="%">
            <template #prefix><el-icon color="#e6a23c"><TrendCharts /></el-icon></template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <el-statistic title="未达标预警" :value="8">
            <template #prefix><el-icon color="#f56c6c"><Warning /></el-icon></template>
          </el-statistic>
        </el-card>
      </el-col>
    </el-row>

    <!-- 企业减排排行 -->
    <el-card style="margin-top: 20px">
      <template #header>企业减排排行</template>
      <el-table :data="tableData" border stripe>
        <el-table-column type="index" label="排名" width="60" />
        <el-table-column prop="enterprise" label="企业名称" />
        <el-table-column prop="target" label="减排目标(万吨)" />
        <el-table-column prop="completed" label="已完成(万吨)" />
        <el-table-column prop="rate" label="完成率">
          <template #default="{ row }">
            <el-progress :percentage="row.rate" :status="row.rate >= 80 ? 'success' : row.rate >= 50 ? '' : 'exception'" />
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '达标' ? 'success' : 'warning'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Aim, CircleCheckFilled, TrendCharts, Warning } from '@element-plus/icons-vue'

const tableData = ref([
  { enterprise: '宝钢股份', target: 80, completed: 72, rate: 90, status: '达标' },
  { enterprise: '鞍钢集团', target: 75, completed: 62, rate: 83, status: '达标' },
  { enterprise: '华能国际', target: 60, completed: 45, rate: 75, status: '达标' },
  { enterprise: '海螺水泥', target: 55, completed: 38, rate: 69, status: '达标' },
  { enterprise: '中石化', target: 70, completed: 42, rate: 60, status: '达标' },
  { enterprise: '大唐电力', target: 50, completed: 28, rate: 56, status: '达标' },
  { enterprise: '河钢集团', target: 45, completed: 18, rate: 40, status: '未达标' },
  { enterprise: '中国建材', target: 40, completed: 7, rate: 18, status: '未达标' }
])
</script>

<style scoped>
.stat-card {
  text-align: center;
}
</style>
