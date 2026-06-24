<template>
  <div class="dashboard-container">
    <h1 style="margin-bottom: 20px">数据概览仪表盘</h1>

    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="8"><el-card shadow="hover" class="stat-card"><el-statistic title="累计碳排放量（吨）" :value="totalEmission"/></el-card></el-col>
      <el-col :span="8"><el-card shadow="hover" class="stat-card"><el-statistic title="减排完成率" :value="reductionRate" suffix="%"/></el-card></el-col>
      <el-col :span="8"><el-card shadow="hover" class="stat-card"><el-statistic title="检测企业数量" :value="companyCount"/></el-card></el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12"><el-card shadow="hover"><template #header>月度碳排放量趋势</template><div ref="trendChart" style="height: 360px"></div></el-card></el-col>
      <el-col :span="12"><el-card shadow="hover"><template #header>碳排放来源占比</template><div ref="pieChart" style="height: 360px"></div></el-card></el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import * as echarts from 'echarts'
import { useAuthStore } from '@/stores/auth.js'

const authStore = useAuthStore()
const trendChart = ref(null)
const pieChart = ref(null)
const totalEmission = ref(0)
const reductionRate = ref(0)
const companyCount = ref(0)

let trendInstance = null, pieInstance = null
let resizeHandler = null

const fetchData = async () => {
  try {
    const headers = { 'Authorization': authStore.getAuthorizationHeader() }
    const res = await fetch('/api/dashboard/overview', { headers })
    const result = await res.json()
    if (result.code === 200 && result.data) {
      const d = result.data
      totalEmission.value = d.totalEmission || 0
      reductionRate.value = d.reductionRate || 0
      companyCount.value = d.companyCount || 0

      // 月度趋势
      const months = (d.monthlyEmission || []).map(i => i.month + '月')
      const values = (d.monthlyEmission || []).map(i => Number(i.total) || 0)

      // 来源占比
      let sourceData = (d.sourceData || []).map(s => ({
        name: s.name || s.source_name || '',
        value: Number(s.value || s.total || 0)
      }))

      if (!sourceData.length) sourceData = [{ name: '暂无数据', value: 1 }]

      await nextTick()
      initCharts(months, values, sourceData)
    }
  } catch (e) {
    console.error('Dashboard数据加载失败:', e)
    // 兜底空数据
    totalEmission.value = 0
    reductionRate.value = 0
    companyCount.value = 0
  }
}

const initCharts = (months, values, sourceData) => {
  // 折线图
  if (trendChart.value) {
    if (trendInstance) trendInstance.dispose()
    trendInstance = echarts.init(trendChart.value)
    trendInstance.setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: '50px', right: '20px', top: '20px', bottom: '30px' },
      xAxis: { type: 'category', data: months.length ? months : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'] },
      yAxis: { type: 'value', name: '碳排放(吨)' },
      series: [{ name: '碳排放量', type: 'line', smooth: true, data: values,
        areaStyle: { color: 'rgba(64,158,255,0.15)' } }]
    })
  }
  // 饼图
  if (pieChart.value) {
    if (pieInstance) pieInstance.dispose()
    pieInstance = echarts.init(pieChart.value)
    pieInstance.setOption({
      tooltip: { trigger: 'item' },
      legend: { top: 'bottom' },
      series: [{ name: '来源占比', type: 'pie', radius: ['40%','70%'],
        itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 },
        label: { show: true, formatter: '{b}\n{d}%' }, data: sourceData }]
    })
  }
}

onMounted(() => {
  fetchData()
  resizeHandler = () => {
    trendInstance?.resize()
    pieInstance?.resize()
  }
  window.addEventListener('resize', resizeHandler)
})

onBeforeUnmount(() => {
  trendInstance?.dispose()
  pieInstance?.dispose()
  window.removeEventListener('resize', resizeHandler)
})
</script>

<style scoped>
.dashboard-container { padding: 0; }
.stat-card { text-align: center; }
</style>
