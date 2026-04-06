<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useNotificationStore } from '@/stores/notifications'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()
const photoMap = { 'simos.varias@email.com': '/profiles/simos-varias.jpg' }
const userPhoto = computed(() => photoMap[auth.user?.email] || null)
const notif = useNotificationStore()

const initials = computed(() => {
  const name = auth.user?.fullName || auth.user?.email || ''
  return name.split(' ').map(w => w[0]).join('').substring(0, 2).toUpperCase()
})

const navItems = computed(() => {
  const items = [
    { icon: '🏠', label: 'Home', to: '/', active: route.path === '/' }
  ]
  if (auth.isShipowner) {
    items.push(
      { icon: '🔍', label: 'Talent Search', to: '/search', active: route.path === '/search' },
      { icon: '👤', label: 'Profile', to: '/profile', active: route.path.startsWith('/profile') },
      { icon: '📋', label: 'Shortlist', to: '/shortlists', active: route.path.startsWith('/shortlist') },
      { icon: '🏢', label: 'Agent', to: '/agent', active: route.path === '/agent' }
    )
  } else if (auth.isSeafarer) {
    items.push(
      { icon: '👤', label: 'Profile', to: '/profile', active: route.path.startsWith('/profile') }
    )
  } else if (auth.isAgent) {
    items.push(
      { icon: '📊', label: 'Dashboard', to: '/agent', active: route.path === '/agent' }
    )
  }
  if (auth.user?.role === 'ADMIN') {
    items.push(
      { icon: '🛡️', label: 'Admin', to: '/admin', active: route.path === '/admin' }
    )
  }
  items.push(
    { icon: '💬', label: 'Messages', to: '/messages', active: route.path === '/messages' },
    { icon: '🔔', label: 'Alerts', to: '/notifications', active: route.path === '/notifications', badge: notif.unreadCount },
    { icon: '⚙️', label: 'Settings', to: '/settings', active: route.path === '/settings' }
  )
  return items
})

function handleLogout() {
  auth.logout()
  router.push('/login')
}
</script>

<template>
  <nav class="navbar">
    <div class="navbar-inner container">
      <!-- Logo -->
      <div class="navbar-logo" @click="router.push('/')">
        <img src="/logo.svg" alt="HireHub" class="logo-mark" style="width:34px;height:34px;border-radius:8px;" />
        <input class="search-input" type="text" placeholder="Search" />
      </div>

      <!-- Nav Items -->
      <div class="navbar-nav">
        <router-link
          v-for="item in navItems"
          :key="item.to"
          :to="item.to"
          class="nav-item"
          :class="{ active: item.active }"
        >
          <span class="nav-icon">{{ item.icon }}</span>
          <span v-if="item.badge" class="nav-badge">{{ item.badge }}</span>
          <span class="nav-label">{{ item.label }}</span>
        </router-link>
      </div>

      <!-- User Menu -->
      <div class="navbar-user" @click="handleLogout">
        <img v-if="userPhoto" :src="userPhoto" style="width:28px;height:28px;border-radius:50%;object-fit:cover;" />
        <div v-else class="avatar avatar-sm" :class="'avatar-' + (auth.isSeafarer ? 'seafarer' : auth.isAgent ? 'agent' : 'owner')">{{ initials }}</div>
        <div class="user-info">
          <span class="user-name">{{ auth.user?.fullName }}</span>
          <span class="user-role">{{ auth.user?.role?.replace('_', ' ') }}</span>
        </div>
      </div>
    </div>
  </nav>
</template>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 56px;
  background: var(--color-white);
  border-bottom: 1px solid var(--color-border);
  z-index: 100;
}
.navbar-inner {
  display: flex;
  align-items: center;
  height: 100%;
  gap: var(--space-4);
}
.navbar-logo {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  cursor: pointer;
  flex-shrink: 0;
}
.logo-mark {
  width: 34px;
  height: 34px;
  background: var(--color-primary);
  color: var(--color-white);
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 18px;
}
.search-input {
  width: 200px;
  padding: 6px 12px 6px 32px;
  background: var(--color-surface);
  border: none;
  border-radius: var(--radius-sm);
  font: var(--font-small);
  outline: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='14' height='14' fill='%23999' viewBox='0 0 16 16'%3E%3Cpath d='M11.7 10.3a6 6 0 10-1.4 1.4l4 4a1 1 0 001.4-1.4l-4-4zM2 6.5a4.5 4.5 0 119 0 4.5 4.5 0 01-9 0z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: 10px center;
}
.navbar-nav {
  display: flex;
  align-items: center;
  gap: 2px;
  flex: 1;
  justify-content: center;
}
.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 4px 12px;
  text-decoration: none;
  color: var(--color-text-secondary);
  border-bottom: 2px solid transparent;
  position: relative;
  min-width: 60px;
  transition: color 0.15s;
}
.nav-item:hover { color: var(--color-text-primary); text-decoration: none; }
.nav-item.active {
  color: var(--color-text-primary);
  border-bottom-color: var(--color-text-primary);
}
.nav-icon { font-size: 20px; line-height: 1; }
.nav-label { font: var(--font-caption); margin-top: 1px; }
.nav-badge {
  position: absolute;
  top: 0;
  right: 8px;
  background: var(--color-danger);
  color: var(--color-white);
  font-size: 9px;
  font-weight: 600;
  min-width: 16px;
  height: 16px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
}
.navbar-user {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  cursor: pointer;
  flex-shrink: 0;
  padding: var(--space-1);
  border-radius: var(--radius-md);
}
.navbar-user:hover { background: var(--color-surface); }
.user-info { display: flex; flex-direction: column; }
.user-name { font: var(--font-caption); font-weight: 500; color: var(--color-text-primary); }
.user-role { font: var(--font-caption); color: var(--color-text-tertiary); text-transform: capitalize; }

@media (max-width: 768px) {
  .search-input { display: none; }
  .nav-label { display: none; }
  .nav-item { padding: 4px 8px; min-width: 40px; }
  .user-info { display: none; }
}
</style>




