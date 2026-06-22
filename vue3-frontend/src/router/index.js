import { createRouter, createWebHistory } from 'vue-router'

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
        // 双碳平台页面（占位）
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

// ==================== 路由守卫 ====================
router.beforeEach((to, from, next) => {
  const isLoggedIn = !!localStorage.getItem('loginUser')

  // 如果访问后台页面且未登录，跳转到登录页
  if (to.path.startsWith('/Manager') && !isLoggedIn) {
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register') && isLoggedIn) {
    // 已登录用户访问登录/注册页，直接跳转到后台首页
    next('/Manager/Home')
  } else {
    next()
  }
})

export default router