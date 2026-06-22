<template>
  <div class="validcode-container" @click="drawCode" style="cursor: pointer">
    <canvas ref="canvas" width="100" height="36"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted, defineEmits } from 'vue'

const emit = defineEmits(['update:value'])
const canvas = ref(null)

let codeValue = ''

// 生成随机验证码
const generateCode = () => {
  const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789'
  let result = ''
  for (let i = 0; i < 4; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return result
}

// 绘制验证码
const drawCode = () => {
  const ctx = canvas.value.getContext('2d')
  codeValue = generateCode()

  // 清空画布
  ctx.clearRect(0, 0, 100, 36)

  // 背景
  ctx.fillStyle = '#f0f2f5'
  ctx.fillRect(0, 0, 100, 36)

  // 绘制干扰线
  for (let i = 0; i < 3; i++) {
    ctx.strokeStyle = `rgb(${Math.random() * 255}, ${Math.random() * 255}, ${Math.random() * 255})`
    ctx.beginPath()
    ctx.moveTo(Math.random() * 100, Math.random() * 36)
    ctx.lineTo(Math.random() * 100, Math.random() * 36)
    ctx.stroke()
  }

  // 绘制文字
  for (let i = 0; i < codeValue.length; i++) {
    ctx.fillStyle = `rgb(${Math.random() * 150}, ${Math.random() * 150}, ${Math.random() * 150})`
    ctx.font = `${20 + Math.random() * 6}px Arial`
    ctx.save()
    ctx.translate(20 * i + 15, 24)
    ctx.rotate((Math.random() - 0.5) * 0.4)
    ctx.fillText(codeValue.charAt(i), -8, 6)
    ctx.restore()
  }

  // 发送验证码值给父组件
  emit('update:value', codeValue)
}

onMounted(() => {
  drawCode()
})
</script>

<style scoped>
.validcode-container {
  display: inline-block;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  flex-shrink: 0;
}
</style>
