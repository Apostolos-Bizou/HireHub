<script setup>
const requests = [
  { owner: "Tsakos Energy Navigation", position: "3rd Officer — Oil Tanker", vessel: "MT Athens Glory", candidates: 3, urgency: "Urgent" },
  { owner: "Diana Shipping", position: "Chief Engineer — Bulk", vessel: "MV Diana Explorer", candidates: 5, urgency: "Standard" },
  { owner: "Capital Ship Management", position: "2nd Officer — Container", vessel: "MV Capital Fortune", candidates: 2, urgency: "Urgent" }
]
const pipeline = { received: 3, vetting: 5, contract: 2, deployment: 2 }
</script>
<template>
  <div class="container" style="padding-top:var(--space-5);max-width:1000px">
    <div style="display:flex;align-items:center;gap:var(--space-3);margin-bottom:var(--space-5)">
      <div class="avatar avatar-lg avatar-agent">MA</div>
      <div><h1 style="font:var(--font-h1)">Magsaysay Maritime Corp.</h1><p style="font:var(--font-small);color:var(--color-text-secondary)">DMW-1234-2026 │ 8 principals │ 487 deployments YTD</p></div>
    </div>
    <div style="display:grid;grid-template-columns:repeat(4,1fr);gap:var(--space-3);margin-bottom:var(--space-5)">
      <div class="card metric-card"><div class="value" style="color:var(--color-danger)">3</div><div class="label">New requests</div></div>
      <div class="card metric-card"><div class="value">8</div><div class="label">Active pipelines</div></div>
      <div class="card metric-card"><div class="value">15</div><div class="label">In review</div></div>
      <div class="card metric-card"><div class="value">94%</div><div class="label">Placement rate</div></div>
    </div>
    <h2 style="font:var(--font-h2);margin-bottom:var(--space-3)">Incoming Shortlists</h2>
    <div v-for="r in requests" :key="r.vessel" class="card" style="padding:var(--space-4);margin-bottom:var(--space-3)">
      <div style="display:flex;justify-content:space-between;align-items:start">
        <div>
          <div style="display:flex;align-items:center;gap:var(--space-2)"><div class="avatar avatar-sm avatar-owner">{{ r.owner.split(' ').map(w=>w[0]).join('').substring(0,2) }}</div><strong>{{ r.owner }}</strong></div>
          <p style="font:var(--font-body);margin-top:var(--space-2)">{{ r.position }} — {{ r.vessel }}</p>
        </div>
        <div style="display:flex;gap:var(--space-2);align-items:center">
          <span class="badge" :class="r.urgency==='Urgent'?'badge-danger':'badge-info'">{{ r.urgency }}</span>
          <span class="badge badge-info">{{ r.candidates }} candidates</span>
        </div>
      </div>
      <div style="display:flex;gap:var(--space-2);margin-top:var(--space-3)">
        <button class="btn btn-primary btn-sm">Review candidates</button>
        <button class="btn btn-secondary btn-sm">Message owner</button>
      </div>
    </div>
    <h2 style="font:var(--font-h2);margin:var(--space-5) 0 var(--space-3)">Deployment Pipeline</h2>
    <div style="display:grid;grid-template-columns:repeat(4,1fr);gap:var(--space-3)">
      <div v-for="(v,k) in pipeline" :key="k" class="card" style="padding:var(--space-4);text-align:center">
        <div style="font:var(--font-h1);color:var(--color-primary)">{{ v }}</div>
        <div style="font:var(--font-small);color:var(--color-text-secondary);text-transform:capitalize">{{ k }}</div>
      </div>
    </div>
  </div>
</template>
<style scoped>.btn-sm { font-size: 12px; padding: 4px 12px; }</style>
