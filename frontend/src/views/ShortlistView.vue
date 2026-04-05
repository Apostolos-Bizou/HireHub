<script setup>
import { ref } from "vue"
const shortlists = ref([
  { id: "1", title: "3rd Officer — Oil/Chemical Tanker", vesselContext: "MT Pacific Voyager replacement", status: "ACTIVE", candidateCount: 5, sentToAgent: 2 },
  { id: "2", title: "Chief Engineer — Bulk Carrier", vesselContext: "MV Iron Pioneer crew change", status: "ACTIVE", candidateCount: 3, sentToAgent: 0 },
  { id: "3", title: "2nd Officer — LPG Carrier", vesselContext: "Gas Explorer rotation", status: "COMPLETED", candidateCount: 8, sentToAgent: 4 }
])
</script>
<template>
  <div class="container" style="padding-top:var(--space-5);max-width:900px">
    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:var(--space-4)">
      <h1 style="font:var(--font-h1)">My Shortlists</h1>
      <button class="btn btn-primary">+ New Shortlist</button>
    </div>
    <div v-for="s in shortlists" :key="s.id" class="card" style="padding:var(--space-4);margin-bottom:var(--space-3);cursor:pointer">
      <div style="display:flex;justify-content:space-between;align-items:center">
        <div>
          <h3 style="font:var(--font-h3)">{{ s.title }}</h3>
          <p style="font:var(--font-small);color:var(--color-text-secondary)">{{ s.vesselContext }}</p>
        </div>
        <span class="badge" :class="s.status==='ACTIVE'?'badge-info':'badge-verified'">{{ s.status }}</span>
      </div>
      <div style="display:flex;gap:var(--space-5);margin-top:var(--space-3)">
        <div class="metric-card"><div class="value">{{ s.candidateCount }}</div><div class="label">Candidates</div></div>
        <div class="metric-card"><div class="value">{{ s.sentToAgent }}</div><div class="label">Sent to agent</div></div>
      </div>
    </div>
  </div>
</template>
