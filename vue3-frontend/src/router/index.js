import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/Manager',
      name: 'Manager',
      component: () => import('../views/Manager.vue'),
      redirect: '/Manager/Home',
      children: [
        { path: 'Home', name: 'ManagerHome', component: () => import('../views/Home.vue') },
        { path: 'Data', name: 'ManagerData', component: () => import('../views/Data.vue') },
      ]
    },
  ],
})
export default router