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
      else if (auth.user?.role === 'SEAFARER') next({ name: 'SeafarerHome' })
      else next()
    },
    component: () => import('@/views/HomeView.vue')
  },
  {
    path: '/seafarer-dashboard',
    name: 'SeafarerHome',
    component: () => import('@/views/SeafarerHomeView.vue'),
    meta: { requiresAuth: true, roles: ['SEAFARER', 'ADMIN'] }
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
  {
    path: '/seafarer/:id',
    name: 'SeafarerPublic',
    component: () => import('@/views/SeafarerPublicView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/company/:id',
    name: 'CompanyPublic',
    component: () => import('@/views/CompanyPublicView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/ai-career',
    name: 'AiCareer',
    component: () => import('@/views/SeafarerAiCareerView.vue'),
    meta: { requiresAuth: true, roles: ['SEAFARER', 'ADMIN'] }
  },
  {
    path: '/salary',
    name: 'SalaryBenchmark',
    component: () => import('@/views/SalaryBenchmarkView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/guide',
    name: 'Guide',
    component: () => import('@/views/GuideView.vue'),
    meta: { requiresAuth: true }
  },
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
  {
    path: '/profile/:id',
    redirect: to => ({ name: 'SeafarerPublic', params: { id: to.params.id } })
  },
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

  // Αν έρχεται στο /login με token από landing page — άφσε το να περάσει
  // και το LoginView.vue θα κάνει auto-login μέσω onMounted
  if (to.name === 'Login' && to.query.token) {
    // Κάνε auto-login εδώ στο router guard
    const token = to.query.token
    const refresh = to.query.refresh
    const userRaw = to.query.user
    if (token && userRaw) {
      try {
        const user = JSON.parse(decodeURIComponent(userRaw))
        localStorage.setItem('hirehub_token', token)
        localStorage.setItem('hirehub_refresh', refresh || '')
        localStorage.setItem('hirehub_user', JSON.stringify(user))
        auth.token = token
        auth.user = user
        // Redirect βάσει ρόλου
        const role = user.role?.toUpperCase()
        if (role === 'SEAFARER') return next({ name: 'SeafarerHome' })
        if (role === 'SHIPOWNER') return next({ name: 'ShipownerHome' })
        if (role === 'MANNING_AGENT') return next({ name: 'AgentDashboard' })
        return next({ name: 'Home' })
      } catch (e) {
        console.error('Token parse error:', e)
      }
    }
  }

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
