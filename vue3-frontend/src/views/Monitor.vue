<template>
  <div>
    <h1 style="margin-bottom: 20px">数据监测面板</h1>

    <!-- 指标卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <el-statistic title="今日新增预警" :value="12">
            <template #prefix><el-icon color="#f56c6c"><Warning /></el-icon></template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <el-statistic title="今日碳排放量(吨)" :value="2458">
            <template #prefix><el-icon color="#e6a23c"><Odometer /></el-icon></template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <el-statistic title="设备在线率" :value="96.8" suffix="%">
            <template #prefix><el-icon color="#67c23a"><Connection /></el-icon></template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <el-statistic title="重点监测项目" :value="38">
            <template #prefix><el-icon color="#409eff"><Flag /></el-icon></template>
          </el-statistic>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>行业碳排放对比</template>
          <div ref="barChart" style="height: 350px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>近7日碳排放变化趋势</template>
          <div ref="lineChart" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Warning, Odometer, Connection, Flag } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const barChart = ref(null)
const lineChart = ref(null)

onMounted(() => {
  // 行业碳排放对比柱状图
  const barInstance = echarts.init(barChart.value)
  barInstance.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      data: ['钢铁', '化工', '电力', '水泥', '交通', '建筑']
    },
    yAxis: { type: 'value', name: '碳排放(万吨)' },
    series: [{
      name: '碳排放量',
      type: 'bar',
      data: [320, 280, 450, 200, 180, 150],
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#83bff6' },
          { offset: 1, color: '#188df0' }
        ])
      }
    }]
  })

  // 近7日碳排放变化折线图
  const lineInstance = echarts.init(lineChart.value)
  lineInstance.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      data: ['6/16', '6/17', '6/18', '6/19', '6/20', '6/21', '6/22']
    },
    yAxis: { type: 'value', name: '碳排放(吨)' },
    series: [{
      name: '碳排放',
      type: 'line',
      smooth: true,
      data: [2100, 2350, 2200, 2580, 2420, 2458, 2380],
      areaStyle: { color: 'rgba(64, 158, 255, 0.2)' }
    }]
  })
})
</script>

<style scoped>
.stat-card {
  text-align: center;
}
</style>
