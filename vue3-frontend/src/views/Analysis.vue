<template>
  <div>
    <h1 style="margin-bottom: 20px">碳排放分析</h1>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card>
          <template #header>
            <span>全国碳排放热力分布</span>
          </template>
          <div ref="mapChart" style="height: 450px">
            <div style="display: flex; justify-content: center; align-items: center; height: 100%; color: #909399">
              热力分布图（需要地图组件支持，此处为演示占位）
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>Top10 企业碳排放排行</template>
          <div ref="rankChart" style="height: 450px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="6">
        <el-card>
          <template #header>行业筛选</template>
          <el-select v-model="selectedIndustry" placeholder="请选择行业" style="width: 100%">
            <el-option label="全部行业" value="all" />
            <el-option label="钢铁" value="steel" />
            <el-option label="化工" value="chemical" />
            <el-option label="电力" value="power" />
            <el-option label="水泥" value="cement" />
            <el-option label="交通" value="traffic" />
            <el-option label="建筑" value="building" />
          </el-select>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <template #header>排序方式</template>
          <el-select v-model="sortType" placeholder="请选择排序" style="width: 100%">
            <el-option label="碳排放从高到低" value="desc" />
            <el-option label="碳排放从低到高" value="asc" />
            <el-option label="按企业名称" value="name" />
          </el-select>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const rankChart = ref(null)
const selectedIndustry = ref('all')
const sortType = ref('desc')

onMounted(() => {
  const instance = echarts.init(rankChart.value)
  instance.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'value', name: '碳排放(万吨)' },
    yAxis: {
      type: 'category',
      data: ['鞍钢集团', '中石化', '大唐电力', '海螺水泥', '宝钢股份', '华能国际', '中国建材', '中石油', '国家能源', '河钢集团']
    },
    series: [{
      name: '碳排放',
      type: 'bar',
      data: [180, 165, 152, 140, 128, 115, 105, 98, 85, 72],
      itemStyle: { color: '#67c23a' },
      label: { show: true, position: 'right' }
    }]
  })
})
</script>
