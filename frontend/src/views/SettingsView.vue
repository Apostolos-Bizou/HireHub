<script setup>
import { useAuthStore } from "@/stores/auth"
import { useRouter } from "vue-router"
const auth = useAuthStore()
const router = useRouter()
function handleLogout() { auth.logout(); router.push("/login") }
</script>
<template>
  <div class="container" style="padding-top:var(--space-5);max-width:600px">
    <h1 style="font:var(--font-h1);margin-bottom:var(--space-5)">Settings</h1>
    <div class="card" style="padding:var(--space-5)">
      <h2 style="font:var(--font-h2);margin-bottom:var(--space-4)">Account</h2>
      <div style="margin-bottom:var(--space-3)"><label style="font:var(--font-small);font-weight:500;display:block;margin-bottom:var(--space-1)">Email</label><input class="input" :value="auth.user?.email" readonly /></div>
      <div style="margin-bottom:var(--space-3)"><label style="font:var(--font-small);font-weight:500;display:block;margin-bottom:var(--space-1)">Role</label><input class="input" :value="auth.user?.role" readonly /></div>
      <h2 style="font:var(--font-h2);margin:var(--space-5) 0 var(--space-4)">Privacy</h2>
      <label style="display:flex;align-items:center;gap:var(--space-2);font:var(--font-body);margin-bottom:var(--space-3)"><input type="checkbox" checked /> Profile visible to employers</label>
      <label style="display:flex;align-items:center;gap:var(--space-2);font:var(--font-body);margin-bottom:var(--space-3)"><input type="checkbox" checked /> Allow AI matching</label>
      <label style="display:flex;align-items:center;gap:var(--space-2);font:var(--font-body);margin-bottom:var(--space-3)"><input type="checkbox" /> Show salary expectations</label>
      <button class="btn btn-tertiary" style="margin-top:var(--space-5)" @click="handleLogout">Sign out</button>
    </div>
  </div>
</template>
