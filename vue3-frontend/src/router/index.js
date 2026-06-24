import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/Login', name: 'Login', component: () => import('../views/Login.vue') },
    { path: '/Register', name: 'Register', component: () => import('../views/Register.vue') },
    {
      path: '/Manager',
      name: 'Manager',
      component: () => import('../views/Manager.vue'),
      redirect: '/Manager/Dashboard',
      children: [
        { path: 'Dashboard', name: 'Dashboard', component: () => import('../views/Dashboard.vue') },
        { path: 'Data', name: 'Data', component: () => import('../views/Data.vue') },
        { path: 'Company', name: 'Company', component: () => import('../views/Company.vue') },
        { path: 'CarbonEmission', name: 'CarbonEmission', component: () => import('../views/CarbonEmission.vue') },
        { path: 'CarbonTrade', name: 'CarbonTrade', component: () => import('../views/CarbonTrade.vue') },
        { path: 'NewEnergyProject', name: 'NewEnergyProject', component: () => import('../views/NewEnergyProject.vue') },
        { path: 'EnergySavingGoal', name: 'EnergySavingGoal', component: () => import('../views/EnergySavingGoal.vue') },
        { path: 'Role', name: 'Role', component: () => import('../views/Role.vue') },
        { path: 'Permission', name: 'Permission', component: () => import('../views/Permission.vue') },
      ]
    },
    { path: '/', redirect: '/Login' }
  ],
})

const whiteList = ['/Login', '/Register']

router.beforeEach((to, from, next) => {
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'
  if (to.path.startsWith('/Manager') && !isLoggedIn) {
    next('/Login')
  } else if (whiteList.includes(to.path) && isLoggedIn) {
    next('/Manager/Dashboard')
  } else {
    next()
  }
})

export default router
