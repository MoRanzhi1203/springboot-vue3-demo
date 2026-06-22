import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

/**
 * 认证状态管理 Store
 * 管理用户登录状态、token 和用户信息
 */
export const useAuthStore = defineStore('auth', () => {
  // ==================== 状态 ====================
  const user = ref(null)           // 当前登录用户信息
  const token = ref(null)          // 认证 token
  const isLoggedIn = computed(() => !!token.value && !!user.value)

  // ==================== 登录 ====================
  const login = (userData) => {
    // 保存用户信息
    user.value = userData
    // 生成模拟 token（后端未返回 JWT 时使用 Base64 编码用户信息）
    token.value = 'Bearer ' + btoa(JSON.stringify({
      id: userData.id,
      username: userData.username
    }))

    // 持久化到 localStorage
    saveToLocalStorage()
  }

  // ==================== 退出登录 ====================
  const logout = () => {
    user.value = null
    token.value = null

    // 清除 localStorage
    localStorage.removeItem('auth_user')
    localStorage.removeItem('auth_token')
    localStorage.removeItem('loginUser') // 兼容旧版存储
  }

  // ==================== 恢复会话 ====================
  const restoreSession = () => {
    const savedUser = localStorage.getItem('auth_user')
    const savedToken = localStorage.getItem('auth_token')

    if (savedUser && savedToken) {
      try {
        user.value = JSON.parse(savedUser)
        token.value = savedToken
      } catch (e) {
        // 数据损坏，清除
        logout()
      }
    } else {
      // 兼容旧版 loginUser 存储格式
      const legacyUser = localStorage.getItem('loginUser')
      if (legacyUser) {
        try {
          const parsed = JSON.parse(legacyUser)
          login(parsed)
        } catch (e) {
          logout()
        }
      }
    }
  }

  // ==================== 获取认证请求头 ====================
  const getAuthorizationHeader = () => {
    return token.value || ''
  }

  // ==================== 私有方法 ====================
  const saveToLocalStorage = () => {
    if (user.value) {
      localStorage.setItem('auth_user', JSON.stringify(user.value))
    }
    if (token.value) {
      localStorage.setItem('auth_token', token.value)
    }
  }

  return {
    user,
    token,
    isLoggedIn,
    login,
    logout,
    restoreSession,
    getAuthorizationHeader
  }
})
