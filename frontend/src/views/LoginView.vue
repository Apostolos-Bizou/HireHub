<script setup>
import { ref, onMounted } from "vue"
import { useRouter, useRoute } from "vue-router"
import { useAuthStore } from "@/stores/auth"

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()
const email = ref("")
const password = ref("")
const error = ref("")
const loading = ref(false)

function redirectByRole(role) {
  const r = role?.toUpperCase()
  if (r === 'SEAFARER')      return router.push({ name: 'SeafarerHome' })
  if (r === 'SHIPOWNER')     return router.push({ name: 'ShipownerHome' })
  if (r === 'MANNING_AGENT') return router.push({ name: 'AgentDashboard' })
  router.push('/')
}

onMounted(async () => {
  // Auto-login από landing page token
  if (route.query.token && route.query.user) {
    try {
      const user = JSON.parse(decodeURIComponent(route.query.user))
      localStorage.setItem('hirehub_token', route.query.token)
      localStorage.setItem('hirehub_refresh', route.query.refresh || '')
      localStorage.setItem('hirehub_user', JSON.stringify(user))
      auth.token = route.query.token
      auth.user = user
      return redirectByRole(user.role)
    } catch(e) { console.error(e) }
  }

  // Auto-login από landing page email/pass redirect
  if (route.query.email && route.query.pass) {
    email.value = decodeURIComponent(route.query.email)
    password.value = decodeURIComponent(route.query.pass)
    await handleLogin()
  }
})

async function handleLogin() {
  error.value = ""
  loading.value = true
  try {
    await auth.login(email.value, password.value)
    redirectByRole(auth.user?.role)
  } catch (e) {
    error.value = e.response?.data?.message || "Invalid email or password"
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-card card">
      <div class="logo-mark" style="width:48px;height:48px;font-size:24px;margin:0 auto var(--space-5)">H</div>
      <h1>Sign in to HireHub</h1>
      <p class="subtitle">The Professional Network for Maritime Crew</p>
      <div v-if="loading" style="text-align:center;padding:20px;color:var(--color-text-secondary)">
        Signing in...
      </div>
      <template v-else>
        <div v-if="error" class="error-msg">{{ error }}</div>
        <div class="form-group">
          <label>Email</label>
          <input class="input" v-model="email" type="email" placeholder="your@email.com" @keyup.enter="handleLogin" />
        </div>
        <div class="form-group">
          <label>Password</label>
          <input class="input" v-model="password" type="password" placeholder="Password" @keyup.enter="handleLogin" />
        </div>
        <button class="btn btn-primary" style="width:100%;margin-top:var(--space-4)" :disabled="loading" @click="handleLogin">
          Sign in
        </button>
        <p class="switch-link">New to HireHub? <router-link to="/register">Create account</router-link></p>
      </template>
    </div>
  </div>
</template>

<style scoped>
.auth-page { min-height: 100vh; display: flex; align-items: center; justify-content: center; background: var(--color-surface); }
.auth-card { width: 400px; padding: var(--space-6); text-align: center; }
.auth-card h1 { font: var(--font-h1); margin-bottom: var(--space-1); }
.subtitle { font: var(--font-small); color: var(--color-text-secondary); margin-bottom: var(--space-5); }
.form-group { text-align: left; margin-bottom: var(--space-3); }
.form-group label { display: block; font: var(--font-small); font-weight: 500; margin-bottom: var(--space-1); }
.error-msg { background: var(--color-danger-bg); color: var(--color-danger); padding: var(--space-2) var(--space-3); border-radius: var(--radius-md); font: var(--font-small); margin-bottom: var(--space-3); }
.switch-link { font: var(--font-small); color: var(--color-text-secondary); margin-top: var(--space-4); }
.logo-mark { background: var(--color-primary); color: var(--color-white); border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; font-weight: 700; }
</style>
