<script setup>
import { ref } from "vue"
import { useRoute } from "vue-router"
const route = useRoute()
const tabs = ["All (5)","Shortlisted (2)","Under review (1)","Sent to agent (2)"]
const activeTab = ref(0)
const candidates = ref([
  { name: "Juan Dela Cruz", rank: "3rd Officer", nationality: "Filipino", years: 4, verified: true, aiScore: 92, status: "SHORTLISTED" },
  { name: "Ruslan Goncharov", rank: "3rd Officer", nationality: "Ukrainian", years: 6, verified: true, aiScore: 87, status: "SENT_TO_AGENT" },
  { name: "Andrei Marinescu", rank: "3rd Officer", nationality: "Romanian", years: 3, verified: true, aiScore: 81, status: "UNDER_REVIEW" },
  { name: "Paolo Castillo", rank: "3rd Officer", nationality: "Filipino", years: 5, verified: false, aiScore: 76, status: "SENT_TO_AGENT" },
  { name: "Lazar Stoyanov", rank: "3rd Officer", nationality: "Bulgarian", years: 2, verified: false, aiScore: 68, status: "SHORTLISTED" }
])
function scoreClass(s) { return s >= 85 ? "ai-score-high" : s >= 70 ? "ai-score-mid" : "ai-score-low" }
function statusBadge(s) { return s === 'SENT_TO_AGENT' ? 'badge-verified' : s === 'UNDER_REVIEW' ? 'badge-warning' : 'badge-info' }
</script>
<template>
  <div class="container" style="padding-top:var(--space-5);max-width:960px">
    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:var(--space-4)">
      <div><h1 style="font:var(--font-h1)">3rd Officer — Oil/Chemical Tanker</h1><p style="font:var(--font-small);color:var(--color-text-secondary)">MT Pacific Voyager replacement</p></div>
      <div style="display:flex;gap:var(--space-2)"><button class="btn btn-secondary">Compare</button><button class="btn btn-primary">Forward to Agent</button></div>
    </div>
    <div class="card" style="padding:0">
      <div style="display:flex;border-bottom:1px solid var(--color-border)">
        <button v-for="(t,i) in tabs" :key="i" @click="activeTab=i" class="tab-btn" :class="{active:activeTab===i}">{{ t }}</button>
      </div>
      <div v-for="c in candidates" :key="c.name" style="display:flex;align-items:center;gap:var(--space-3);padding:var(--space-3) var(--space-4);border-bottom:1px solid var(--color-border)">
        <input type="checkbox" />
        <div class="avatar avatar-md avatar-seafarer">{{ c.name.split(' ').map(w=>w[0]).join('') }}</div>
        <div style="flex:1">
          <strong>{{ c.name }}</strong> <span v-if="c.verified" class="badge badge-verified" style="margin-left:4px">✓</span>
          <p style="font:var(--font-small);color:var(--color-text-secondary)">{{ c.rank }} │ {{ c.nationality }} │ {{ c.years }} yrs</p>
        </div>
        <div class="ai-score" :class="scoreClass(c.aiScore)">{{ c.aiScore }}</div>
        <span class="badge" :class="statusBadge(c.status)">{{ c.status.replace(/_/g,' ') }}</span>
      </div>
    </div>
    <div class="card" style="padding:var(--space-4);margin-top:var(--space-3)">
      <h3 style="font:var(--font-h3);margin-bottom:var(--space-2)">Manning Agent</h3>
      <p><strong>Magsaysay Maritime Corp.</strong> · DMW-1234-2026 · Manila</p>
      <div style="display:flex;gap:var(--space-4);margin-top:var(--space-3)">
        <div class="metric-card"><div class="value">5</div><div class="label">Total</div></div>
        <div class="metric-card"><div class="value">2</div><div class="label">Sent</div></div>
        <div class="metric-card"><div class="value">1</div><div class="label">Under review</div></div>
        <div class="metric-card"><div class="value">85</div><div class="label">Avg AI score</div></div>
      </div>
    </div>
  </div>
</template>
<style scoped>
.tab-btn { flex:1; padding: var(--space-3); background:none; border:none; font:var(--font-small); font-weight:500; color:var(--color-text-secondary); cursor:pointer; border-bottom: 2px solid transparent; }
.tab-btn.active { color:var(--color-primary); border-bottom-color:var(--color-primary); }
.tab-btn:hover { background:var(--color-surface); }
</style>
