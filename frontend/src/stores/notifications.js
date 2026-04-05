import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '@/services/api'

export const useNotificationStore = defineStore('notifications', () => {
  const items = ref([])
  const unreadCount = ref(0)

  async function fetchNotifications(page = 0) {
    const { data } = await api.get('/notifications', { params: { page, size: 20 } })
    items.value = data.content || []
    return data
  }

  async function fetchUnreadCount() {
    const { data } = await api.get('/notifications/unread-count')
    unreadCount.value = data.unreadCount || 0
  }

  async function markAsRead(id) {
    await api.patch('/notifications/' + id + '/read')
    unreadCount.value = Math.max(0, unreadCount.value - 1)
  }

  return { items, unreadCount, fetchNotifications, fetchUnreadCount, markAsRead }
})
