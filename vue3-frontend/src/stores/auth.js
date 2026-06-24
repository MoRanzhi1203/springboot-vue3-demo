import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

/**
 * 认证状态管理 Store
 * 管理 JWT Token 和用户信息
 */
export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const token = ref(null)
  const isLoggedIn = computed(() => !!token.value && !!user.value)

  /**
   * 登录 — 接收后端返回的 { token, user }
   */
  const login = (loginData) => {
    // loginData 结构: { token: "xxx", user: { id, username, name, ... } }
    user.value = loginData.user
    token.value = loginData.token
    localStorage.setItem('isLoggedIn', 'true')
    saveToLocalStorage()
  }

  /**
   * 退出登录
   */
  const logout = () => {
    user.value = null
    token.value = null
    localStorage.removeItem('auth_user')
    localStorage.removeItem('auth_token')
    localStorage.removeItem('isLoggedIn')
  }

  /**
   * 从 localStorage 恢复登录会话
   */
  const restoreSession = () => {
    const savedUser = localStorage.getItem('auth_user')
    const savedToken = localStorage.getItem('auth_token')
    if (savedUser && savedToken) {
      try {
        user.value = JSON.parse(savedUser)
        token.value = savedToken
        localStorage.setItem('isLoggedIn', 'true')
      } catch (e) {
        logout()
      }
    }
  }

  /**
   * 获取 Authorization 请求头
   */
  const getAuthorizationHeader = () => {
    if (!token.value) return ''
    // token 已带 Bearer 则直接返回，否则加前缀
    return token.value.startsWith('Bearer ') ? token.value : 'Bearer ' + token.value
  }

  const saveToLocalStorage = () => {
    if (user.value) localStorage.setItem('auth_user', JSON.stringify(user.value))
    if (token.value) localStorage.setItem('auth_token', token.value)
  }

  return { user, token, isLoggedIn, login, logout, restoreSession, getAuthorizationHeader }
})
