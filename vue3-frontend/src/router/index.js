import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 登录页面（独立布局）
    { path: '/login', name: 'Login', component: () => import('../views/Login.vue') },
    // 注册页面（独立布局）
    { path: '/register', name: 'Register', component: () => import('../views/Register.vue') },
    // 后台管理布局
    {
      path: '/Manager',
      name: 'Manager',
      component: () => import('../views/Manager.vue'),
      redirect: '/Manager/Home',
      children: [
        { path: 'Home', name: 'ManagerHome', component: () => import('../views/Home.vue') },
        { path: 'Data', name: 'ManagerData', component: () => import('../views/Data.vue') },
        { path: 'Monitor', name: 'Monitor', component: () => import('../views/Monitor.vue') },
        { path: 'Analysis', name: 'Analysis', component: () => import('../views/Analysis.vue') },
        { path: 'Emission', name: 'Emission', component: () => import('../views/Emission.vue') },
        { path: 'Role', name: 'Role', component: () => import('../views/Placeholder.vue') },
        { path: 'Permission', name: 'Permission', component: () => import('../views/Placeholder.vue') },
        { path: 'Log', name: 'Log', component: () => import('../views/Placeholder.vue') },
      ]
    },
    // 默认重定向到登录页
    { path: '/', redirect: '/login' }
  ],
})

// ==================== 路由白名单 ====================
// 无需登录即可访问的页面
const whiteList = ['/login', '/register']

// ==================== 路由守卫 ====================
router.beforeEach((to, from, next) => {
  // 优先读取 localStorage 判断登录状态，避免 Pinia 初始化时序问题
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'

  // 如果访问后台页面且未登录，跳转到登录页
  if (to.path.startsWith('/Manager') && !isLoggedIn) {
    next('/login')
  } else if (whiteList.includes(to.path) && isLoggedIn) {
    // 已登录用户访问登录/注册页，直接跳转到后台首页
    next('/Manager/Home')
  } else {
    next()
  }
})

export default router