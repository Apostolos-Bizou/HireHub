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
    meta: { requiresAuth: true },
    beforeEnter: (to, from, next) => {
      const auth = useAuthStore()
      if (auth.user?.role === 'SHIPOWNER') next({ name: 'ShipownerHome' })
      else next()
    },
    component: () => import('@/views/HomeView.vue')
  },
  {
    path: '/dashboard',
    name: 'ShipownerHome',
    component: () => import('@/views/ShipownerHomeView.vue'),
    meta: { requiresAuth: true, roles: ['SHIPOWNER', 'MANNING_AGENT', 'ADMIN'] }
  },
  {
    path: '/search',
    name: 'TalentSearch',
    component: () => import('@/views/TalentSearchView.vue'),
    meta: { requiresAuth: true, roles: ['SHIPOWNER', 'MANNING_AGENT', 'ADMIN'] }
  },
  {
    path: '/ai-recommender',
    name: 'AiRecommender',
    component: () => import('@/views/AiRecommenderView.vue'),
    meta: { requiresAuth: true, roles: ['SHIPOWNER', 'MANNING_AGENT', 'ADMIN'] }
  },
  // ===== PROFILE ROUTING =====
  // Viewing a SEAFARER's public profile (Shipowner/Agent clicks on candidate)
  {
    path: '/seafarer/:id',
    name: 'SeafarerPublic',
    component: () => import('@/views/SeafarerPublicView.vue'),
    meta: { requiresAuth: true }
  },
  // Viewing a COMPANY's public profile (Seafarer clicks on a company)
  {
    path: '/company/:id',
    name: 'CompanyPublic',
    component: () => import('@/views/CompanyPublicView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/guide',
    name: 'Guide',
    component: () => import('@/views/GuideView.vue'),
    meta: { requiresAuth: true }
  },
  // Own profile — redirects based on role
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
    meta: { requiresAuth: true, roles: ['SHIPOWNER', 'MANNING_AGENT', 'ADMIN'] }
  },
  {
    path: '/my-profile/agency',
    name: 'AgentProfile',
    component: () => import('@/views/AgentDashboardView.vue'),
    meta: { requiresAuth: true, roles: ['MANNING_AGENT', 'ADMIN'] }
  },
  // Legacy /profile/:id route — redirect to /seafarer/:id
  {
    path: '/profile/:id',
    redirect: to => ({ name: 'SeafarerPublic', params: { id: to.params.id } })
  },
  // ===== END PROFILE ROUTING =====
  {
    path: '/shortlists',
    name: 'Shortlists',
    component: () => import('@/views/ShortlistView.vue'),
    meta: { requiresAuth: true, roles: ['SHIPOWNER', 'MANNING_AGENT', 'ADMIN'] }
  },
  {
    path: '/shortlists/:id',
    name: 'ShortlistDetail',
    component: () => import('@/views/ShortlistDetailView.vue'),
    meta: { requiresAuth: true, roles: ['SHIPOWNER', 'MANNING_AGENT', 'ADMIN'] }
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

