import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useShortlistStore = defineStore('shortlists', () => {
  const shortlists = ref([
    { id: "1", title: "3rd Officer — Oil/Chemical Tanker", vesselContext: "MT Pacific Voyager replacement", status: "ACTIVE", candidateCount: 5, sentToAgent: 2, avgScore: 85, created: "Apr 2, 2026", agentName: "Magsaysay Maritime" },
    { id: "2", title: "Chief Engineer — Bulk Carrier", vesselContext: "MV Iron Pioneer crew change", status: "ACTIVE", candidateCount: 3, sentToAgent: 0, avgScore: 78, created: "Apr 3, 2026", agentName: "CF Sharp" },
    { id: "3", title: "2nd Officer — LPG Carrier", vesselContext: "Gas Explorer rotation", status: "COMPLETED", candidateCount: 8, sentToAgent: 4, avgScore: 82, created: "Mar 20, 2026", agentName: "Bernhard Schulte" }
  ])

  function addShortlist(sl) {
    shortlists.value.unshift(sl)
  }

  function addCandidatesToShortlist(shortlistId, count) {
    const sl = shortlists.value.find(s => s.id === shortlistId)
    if (sl) sl.candidateCount += count
  }

  const totalCandidates = computed(() => shortlists.value.reduce((a, s) => a + s.candidateCount, 0))

  return { shortlists, addShortlist, addCandidatesToShortlist, totalCandidates }
})
