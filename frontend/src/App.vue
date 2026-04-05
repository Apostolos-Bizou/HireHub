<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import AppNavbar from '@/components/layout/AppNavbar.vue'

const route = useRoute()
const auth = useAuthStore()
const showNavbar = computed(() => auth.isAuthenticated && !route.meta.hideNav)
</script>

<template>
  <AppNavbar v-if="showNavbar" />
  <main :class="{ 'with-navbar': showNavbar }">
    <router-view />
  </main>
</template>

<style scoped>
.with-navbar {
  padding-top: 60px;
}
</style>
