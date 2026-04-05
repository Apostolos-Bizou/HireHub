import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/services/api'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(JSON.parse(localStorage.getItem('hirehub_user') || 'null'))
  const accessToken = ref(localStorage.getItem('hirehub_token') || null)
  const refreshToken = ref(localStorage.getItem('hirehub_refresh') || null)

  const isAuthenticated = computed(() => !!accessToken.value)
  const isSeafarer = computed(() => user.value?.role === 'SEAFARER')
  const isShipowner = computed(() => user.value?.role === 'SHIPOWNER')
  const isAgent = computed(() => user.value?.role === 'MANNING_AGENT')

  function setAuth(data) {
    accessToken.value = data.accessToken
    refreshToken.value = data.refreshToken
    user.value = data.user
    localStorage.setItem('hirehub_token', data.accessToken)
    localStorage.setItem('hirehub_refresh', data.refreshToken)
    localStorage.setItem('hirehub_user', JSON.stringify(data.user))
  }

  async function login(email, password) {
    const { data } = await api.post('/auth/login', { email, password })
    setAuth(data)
    return data
  }

  async function register(payload) {
    const { data } = await api.post('/auth/register', payload)
    setAuth(data)
    return data
  }

  async function refreshAccessToken() {
    try {
      const { data } = await api.post('/auth/refresh', { refreshToken: refreshToken.value })
      setAuth(data)
      return data.accessToken
    } catch { logout(); return null }
  }

  function logout() {
    user.value = null
    accessToken.value = null
    refreshToken.value = null
    localStorage.removeItem('hirehub_token')
    localStorage.removeItem('hirehub_refresh')
    localStorage.removeItem('hirehub_user')
  }

  return { user, accessToken, refreshToken, isAuthenticated, isSeafarer, isShipowner, isAgent, login, register, refreshAccessToken, logout, setAuth }
})
