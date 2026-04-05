<script setup>
import { ref } from "vue"
import { useRouter } from "vue-router"
import { useAuthStore } from "@/stores/auth"

const router = useRouter()
const auth = useAuthStore()
const form = ref({ email: "", password: "", fullName: "", role: "SEAFARER", companyName: "", dmwLicenseNumber: "" })
const error = ref("")
const loading = ref(false)

async function handleRegister() {
  error.value = ""
  loading.value = true
  try {
    await auth.register(form.value)
    router.push("/")
  } catch (e) {
    error.value = e.response?.data?.message || "Registration failed"
  } finally { loading.value = false }
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-card card">
      <div class="logo-mark" style="width:48px;height:48px;font-size:24px;margin:0 auto var(--space-5)">H</div>
      <h1>Join HireHub</h1>
      <p class="subtitle">Create your maritime professional account</p>
      <div v-if="error" class="error-msg">{{ error }}</div>
      <div class="form-group">
        <label>I am a</label>
        <select class="select" v-model="form.role">
          <option value="SEAFARER">Seafarer</option>
          <option value="SHIPOWNER">Shipowner / Fleet Manager</option>
          <option value="MANNING_AGENT">Manning Agent</option>
        </select>
      </div>
      <div class="form-group">
        <label>Full Name</label>
        <input class="input" v-model="form.fullName" placeholder="Your full name" />
      </div>
      <div class="form-group" v-if="form.role !== 'SEAFARER'">
        <label>Company Name</label>
        <input class="input" v-model="form.companyName" placeholder="Company name" />
      </div>
      <div class="form-group" v-if="form.role === 'MANNING_AGENT'">
        <label>DMW License Number</label>
        <input class="input" v-model="form.dmwLicenseNumber" placeholder="DMW-XXXX-XXXX" />
      </div>
      <div class="form-group">
        <label>Email</label>
        <input class="input" v-model="form.email" type="email" placeholder="your@email.com" />
      </div>
      <div class="form-group">
        <label>Password</label>
        <input class="input" v-model="form.password" type="password" placeholder="Min. 8 characters" />
      </div>
      <button class="btn btn-primary" style="width:100%;margin-top:var(--space-4)" :disabled="loading" @click="handleRegister">
        {{ loading ? 'Creating...' : 'Create Account' }}
      </button>
      <p class="switch-link">Already on HireHub? <router-link to="/login">Sign in</router-link></p>
    </div>
  </div>
</template>

<style scoped>
.auth-page { min-height: 100vh; display: flex; align-items: center; justify-content: center; background: var(--color-surface); }
.auth-card { width: 440px; padding: var(--space-6); text-align: center; }
.auth-card h1 { font: var(--font-h1); margin-bottom: var(--space-1); }
.subtitle { font: var(--font-small); color: var(--color-text-secondary); margin-bottom: var(--space-5); }
.form-group { text-align: left; margin-bottom: var(--space-3); }
.form-group label { display: block; font: var(--font-small); font-weight: 500; margin-bottom: var(--space-1); }
.error-msg { background: var(--color-danger-bg); color: var(--color-danger); padding: var(--space-2) var(--space-3); border-radius: var(--radius-md); font: var(--font-small); margin-bottom: var(--space-3); }
.switch-link { font: var(--font-small); color: var(--color-text-secondary); margin-top: var(--space-4); }
.logo-mark { background: var(--color-primary); color: var(--color-white); border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; font-weight: 700; }
</style>
