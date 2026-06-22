<template>
  <div class="dashboard-container">
    <h1 style="margin-bottom: 20px">数据概览仪表盘</h1>

    <!-- 指标卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <el-statistic title="累计碳排放量(万吨)" :value="12856">
            <template #prefix>
              <el-icon color="#f56c6c"><TrendCharts /></el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <el-statistic title="减排完成率" :value="62.4" suffix="%">
            <template #prefix>
              <el-icon color="#67c23a"><CircleCheckFilled /></el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <el-statistic title="监测企业数量" :value="156">
            <template #prefix>
              <el-icon color="#409eff"><OfficeBuilding /></el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <el-statistic title="监测设备在线数" :value="1245">
            <template #prefix>
              <el-icon color="#e6a23c"><Connection /></el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="14">
        <el-card shadow="hover">
          <template #header>
            <span>月度碳排放趋势图</span>
          </template>
          <div ref="trendChart" style="height: 350px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="hover">
          <template #header>
            <span>碳排放来源占比</span>
          </template>
          <div ref="pieChart" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { TrendCharts, CircleCheckFilled, OfficeBuilding, Connection } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const trendChart = ref(null)
const pieChart = ref(null)

onMounted(() => {
  // 月度碳排放趋势图
  const trendInstance = echarts.init(trendChart.value)
  trendInstance.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    },
    yAxis: { type: 'value', name: '碳排放(万吨)' },
    series: [
      {
        name: '2024年',
        type: 'line',
        data: [980, 920, 1050, 1100, 1080, 1150, 1200, 1180, 1120, 1050, 1000, 970],
        smooth: true,
        areaStyle: { color: 'rgba(64, 158, 255, 0.15)' }
      },
      {
        name: '2025年',
        type: 'line',
        data: [940, 880, 1000, 1050, 1020, 1080, 1120, 1100, 1050, 990, 950, 920],
        smooth: true,
        areaStyle: { color: 'rgba(103, 194, 58, 0.15)' }
      },
      {
        name: '目标线',
        type: 'line',
        data: [900, 850, 950, 1000, 980, 1020, 1050, 1030, 980, 930, 900, 870],
        lineStyle: { type: 'dashed', color: '#e6a23c' },
        itemStyle: { color: '#e6a23c' }
      }
    ]
  })

  // 碳排放来源占比图
  const pieInstance = echarts.init(pieChart.value)
  pieInstance.setOption({
    tooltip: { trigger: 'item' },
    legend: { bottom: '0%', orient: 'horizontal' },
    series: [{
      name: '来源占比',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
      label: { show: true, formatter: '{b}\n{d}%' },
      data: [
        { value: 1048, name: '能源工业' },
        { value: 735, name: '制造业' },
        { value: 580, name: '交通运输' },
        { value: 484, name: '建筑业' },
        { value: 300, name: '农业' },
        { value: 120, name: '其他' }
      ]
    }]
  })
})
</script>

<style scoped>
.dashboard-container {
  padding: 0;
}

.stat-card {
  text-align: center;
}
</style>
