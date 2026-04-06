<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useNotificationStore } from '@/stores/notifications'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()
const photoMap = { 'simos.varias@email.com': '/profiles/simos-varias.jpg', 'info@varship.gr': '/profiles/varship-logo.png' }
const userPhoto = computed(() => photoMap[auth.user?.email] || null)
const notif = useNotificationStore()

const initials = computed(() => {
  const name = auth.user?.fullName || auth.user?.email || ''
  return name.split(' ').map(w => w[0]).join('').substring(0, 2).toUpperCase()
})

const icons = {
  home: '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>',
  search: '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>',
  profile: '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>',
  shortlist: '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"/><rect x="8" y="2" width="8" height="4" rx="1" ry="1"/><path d="M9 14l2 2 4-4"/></svg>',
  agent: '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M2 20h20"/><path d="M5 20V8l7-5 7 5v12"/><path d="M9 20v-6h6v6"/><path d="M9 12h6"/></svg>',
  messages: '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>',
  alerts: '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/></svg>',
  settings: '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="3"/><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06A1.65 1.65 0 0 0 4.68 15a1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06A1.65 1.65 0 0 0 9 4.68a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06A1.65 1.65 0 0 0 19.4 9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"/></svg>',
  admin: '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg>',
  dashboard: '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><line x1="3" y1="9" x2="21" y2="9"/><line x1="9" y1="21" x2="9" y2="9"/></svg>',
  aiRec: '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2a4 4 0 0 1 4 4c0 1.95-1.4 3.58-3.25 3.93L12 22"/><path d="M12 2a4 4 0 0 0-4 4c0 1.95 1.4 3.58 3.25 3.93"/><circle cx="12" cy="6" r="1.5"/></svg>'
}

const navItems = computed(() => {
  const items = [
    { icon: icons.home, label: 'Home', title: 'Αρχική σελίδα — dashboard και επισκόπηση', to: '/', active: route.path === '/' }
  ]
  if (auth.isShipowner) {
    items.push(
      { icon: icons.search, label: 'Talent Search', title: 'Αναζήτηση ναυτικών — φίλτρα, AI score, CrewScore', to: '/search', active: route.path === '/search' },
      { icon: icons.aiRec, label: 'AI Recruit', title: 'AI Σύσταση — αυτόματη αντιστοίχιση υποψηφίων', to: '/ai-recommender', active: route.path === '/ai-recommender' },
      { icon: icons.profile, label: 'Profile', title: 'Το προφίλ σας — στοιχεία, ρυθμίσεις', to: '/profile', active: route.path.startsWith('/profile') || route.path.startsWith('/my-profile') },
      { icon: icons.shortlist, label: 'Shortlist', title: 'Λίστες υποψηφίων — διαχείριση και αποστολή σε agent', to: '/shortlists', active: route.path.startsWith('/shortlist') }
    )
  } else if (auth.isSeafarer) {
    items.push(
      { icon: icons.profile, label: 'Profile', title: 'Το προφίλ σας — στοιχεία, ρυθμίσεις', to: '/profile', active: route.path.startsWith('/profile') }
    )
  } else if (auth.isAgent) {
    items.push(
      { icon: icons.search, label: 'Talent Search', to: '/search', active: route.path === '/search' },
      { icon: icons.aiRec, label: 'AI Recruit', to: '/ai-recommender', active: route.path === '/ai-recommender' },
      { icon: icons.dashboard, label: 'Dashboard', to: '/agent', active: route.path === '/agent' }
    )
  }
  if (auth.user?.role === 'ADMIN') {
    items.push(
      { icon: icons.admin, label: 'Admin', to: '/admin', active: route.path === '/admin' }
    )
  }
  items.push(
    { icon: icons.messages, label: 'Messages', title: 'Μηνύματα — επικοινωνία με ναυτικούς, agents, πλοιοκτήτες', to: '/messages', active: route.path === '/messages' },
    { icon: icons.alerts, label: 'Alerts', title: 'Ειδοποιήσεις — νέα profiles, shortlists, λήξεις', to: '/notifications', active: route.path === '/notifications', badge: notif.unreadCount },
    { icon: icons.settings, label: 'Guide', title: '������ ���������� � ���� ��� ���������� �� HireHub', to: '/guide', active: route.path === '/guide' },
    { icon: icons.settings, label: 'Settings', title: 'Ρυθμίσεις — λογαριασμός, ιδιωτικότητα, προτιμήσεις', to: '/settings', active: route.path === '/settings' }
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
      <div class="navbar-logo" @click="router.push('/')">
        <img src="/logo.svg" alt="HireHub" class="logo-mark" style="width:34px;height:34px;border-radius:8px;" />
        <input class="search-input" type="text" placeholder="Search" />
      </div>

      <div class="navbar-nav">
        <router-link
          v-for="item in navItems" :title="item.title"
          :key="item.to"
          :to="item.to"
          class="nav-item"
          :class="{ active: item.active }"
        >
          <span class="nav-icon" v-html="item.icon"></span>
          <span v-if="item.badge" class="nav-badge">{{ item.badge }}</span>
          <span class="nav-label">{{ item.label }}</span>
        </router-link>
      </div>

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
.nav-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  opacity: 0.5;
}
.nav-item:hover .nav-icon,
.nav-item.active .nav-icon { opacity: 1; }
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



