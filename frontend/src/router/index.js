import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: { hideNav: true, guest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/RegisterView.vue'),
    meta: { hideNav: true, guest: true }
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/HomeView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/search',
    name: 'TalentSearch',
    component: () => import('@/views/TalentSearchView.vue'),
    meta: { requiresAuth: true, roles: ['SHIPOWNER', 'ADMIN'] }
  },
  // Viewing another user's profile (e.g. shipowner clicks on a candidate)
  {
    path: '/profile/:id',
    name: 'ViewProfile',
    component: () => import('@/views/SeafarerProfileView.vue'),
    meta: { requiresAuth: true }
  },
  // Own profile — role-based routing
  {
    path: '/profile',
    name: 'MyProfile',
    meta: { requiresAuth: true },
    beforeEnter: (to, from, next) => {
      const auth = useAuthStore()
      const role = auth.user?.role
      if (role === 'SHIPOWNER') next({ name: 'ShipownerProfile' })
      else if (role === 'MANNING_AGENT') next({ name: 'AgentProfile' })
      else next({ name: 'SeafarerProfile' })
    },
    // Fallback component (won't render due to redirect)
    component: () => import('@/views/SeafarerProfileView.vue')
  },
  {
    path: '/my-profile/seafarer',
    name: 'SeafarerProfile',
    component: () => import('@/views/SeafarerProfileView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/my-profile/company',
    name: 'ShipownerProfile',
    component: () => import('@/views/ShipownerProfileView.vue'),
    meta: { requiresAuth: true, roles: ['SHIPOWNER', 'ADMIN'] }
  },
  {
    path: '/my-profile/agency',
    name: 'AgentProfile',
    component: () => import('@/views/AgentDashboardView.vue'),
    meta: { requiresAuth: true, roles: ['MANNING_AGENT', 'ADMIN'] }
  },
  {
    path: '/shortlists',
    name: 'Shortlists',
    component: () => import('@/views/ShortlistView.vue'),
    meta: { requiresAuth: true, roles: ['SHIPOWNER', 'ADMIN'] }
  },
  {
    path: '/shortlists/:id',
    name: 'ShortlistDetail',
    component: () => import('@/views/ShortlistDetailView.vue'),
    meta: { requiresAuth: true, roles: ['SHIPOWNER', 'ADMIN'] }
  },
  {
    path: '/agent',
    name: 'AgentDashboard',
    component: () => import('@/views/AgentDashboardView.vue'),
    meta: { requiresAuth: true, roles: ['MANNING_AGENT', 'ADMIN'] }
  },
  {
    path: '/analytics',
    name: 'Analytics',
    component: () => import('@/views/AnalyticsDashboardView.vue'),
    meta: { requiresAuth: true, roles: ['SHIPOWNER', 'MANNING_AGENT', 'ADMIN'] }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/AdminView.vue'),
    meta: { requiresAuth: true, roles: ['ADMIN'] }
  },
  {
    path: '/messages',
    name: 'Messages',
    component: () => import('@/views/MessagesView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/notifications',
    name: 'Notifications',
    component: () => import('@/views/NotificationsView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/settings',
    name: 'Settings',
    component: () => import('@/views/SettingsView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

// Navigation guards
router.beforeEach((to, from, next) => {
  const auth = useAuthStore()

  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else if (to.meta.guest && auth.isAuthenticated) {
    next({ name: 'Home' })
  } else if (to.meta.roles && !to.meta.roles.includes(auth.user?.role)) {
    next({ name: 'Home' })
  } else {
    next()
  }
})

export default router
