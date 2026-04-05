import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '@/services/api'

export const useSearchStore = defineStore('search', () => {
  const results = ref(null)
  const loading = ref(false)
  const filters = ref({ rank: '', vesselType: '', nationality: '', minExperience: null, availableBefore: null, verifiedOnly: false, sortBy: 'aiScore', page: 0, size: 20 })

  async function searchCandidates() {
    loading.value = true
    try {
      const { data } = await api.post('/search/candidates', filters.value)
      results.value = data
    } finally { loading.value = false }
  }

  function resetFilters() {
    filters.value = { rank: '', vesselType: '', nationality: '', minExperience: null, availableBefore: null, verifiedOnly: false, sortBy: 'aiScore', page: 0, size: 20 }
    results.value = null
  }

  return { results, loading, filters, searchCandidates, resetFilters }
})
