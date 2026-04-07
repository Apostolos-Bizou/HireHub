import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import api from '@/services/api'

export const useShortlistStore = defineStore('shortlists', () => {
  const shortlists = ref([])
  const loading = ref(false)
  const error = ref(null)

  async function loadShortlists() {
    loading.value = true
    error.value = null
    try {
      const { data } = await api.get('/shortlists')
      // API returns array or page object
      shortlists.value = Array.isArray(data) ? data : (data.content ?? data)
    } catch (e) {
      error.value = e.message
      // Fallback to demo data if API fails
      if (shortlists.value.length === 0) {
        shortlists.value = [
          { id: '1', title: '3rd Officer — Oil/Chemical Tanker', vesselContext: 'MT Pacific Voyager replacement', status: 'ACTIVE', candidateCount: 5, sentToAgent: 2, avgScore: 85, created: 'Apr 2, 2026', agentName: 'Magsaysay Maritime' },
          { id: '2', title: 'Chief Engineer — Bulk Carrier', vesselContext: 'MV Iron Pioneer crew change', status: 'ACTIVE', candidateCount: 3, sentToAgent: 0, avgScore: 78, created: 'Apr 3, 2026', agentName: 'CF Sharp' },
          { id: '3', title: '2nd Officer — LPG Carrier', vesselContext: 'Gas Explorer rotation', status: 'COMPLETED', candidateCount: 8, sentToAgent: 4, avgScore: 82, created: 'Mar 20, 2026', agentName: 'Bernhard Schulte' }
        ]
      }
    } finally {
      loading.value = false
    }
  }

  function addShortlist(sl) {
    shortlists.value.unshift(sl)
  }

  function addCandidatesToShortlist(shortlistId, count) {
    const sl = shortlists.value.find(s => String(s.id) === String(shortlistId))
    if (sl) sl.candidateCount = (sl.candidateCount || 0) + count
  }

  function updateShortlistStatus(shortlistId, sentCount) {
    const sl = shortlists.value.find(s => String(s.id) === String(shortlistId))
    if (sl) sl.sentToAgent = (sl.sentToAgent || 0) + sentCount
  }

  const totalCandidates = computed(() =>
    shortlists.value.reduce((a, s) => a + (s.candidateCount || 0), 0)
  )

  return {
    shortlists, loading, error,
    loadShortlists, addShortlist,
    addCandidatesToShortlist, updateShortlistStatus,
    totalCandidates
  }
})