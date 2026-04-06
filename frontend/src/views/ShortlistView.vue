<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const shortlists = ref([
  { id: "1", title: "3rd Officer — Oil/Chemical Tanker", vesselContext: "MT Pacific Voyager replacement", status: "ACTIVE", candidateCount: 5, sentToAgent: 2, avgScore: 85, created: "Apr 2, 2026", agentName: "Magsaysay Maritime" },
  { id: "2", title: "Chief Engineer — Bulk Carrier", vesselContext: "MV Iron Pioneer crew change", status: "ACTIVE", candidateCount: 3, sentToAgent: 0, avgScore: 78, created: "Apr 3, 2026", agentName: "CF Sharp" },
  { id: "3", title: "2nd Officer — LPG Carrier", vesselContext: "Gas Explorer rotation", status: "COMPLETED", candidateCount: 8, sentToAgent: 4, avgScore: 82, created: "Mar 20, 2026", agentName: "Bernhard Schulte" }
])

function scoreClass(s) { return s >= 85 ? "ai-score-high" : s >= 70 ? "ai-score-mid" : "ai-score-low" }
</script>

<template>
  <div class="container shortlists-page">
    <div class="page-header">
      <div>
        <h1>My Shortlists</h1>
        <p class="page-sub">{{ shortlists.length }} shortlists · {{ shortlists.reduce((a, s) => a + s.candidateCount, 0) }} total candidates</p>
      </div>
      <button class="btn btn-primary">
        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
        New Shortlist
      </button>
    </div>

    <div class="shortlist-list">
      <div
        v-for="s in shortlists"
        :key="s.id"
        class="card shortlist-card"
        @click="router.push(`/shortlists/${s.id}`)"
      >
        <div class="shortlist-top">
          <div class="shortlist-info">
            <h3>{{ s.title }}</h3>
            <p class="shortlist-context">{{ s.vesselContext }} · Created {{ s.created }}</p>
          </div>
          <span class="badge" :class="s.status === 'ACTIVE' ? 'badge-info' : 'badge-verified'">
            {{ s.status === 'ACTIVE' ? 'Active' : 'Completed' }}
          </span>
        </div>

        <div class="shortlist-metrics">
          <div class="sl-metric">
            <div class="sl-metric-value">{{ s.candidateCount }}</div>
            <div class="sl-metric-label">Candidates</div>
          </div>
          <div class="sl-metric">
            <div class="sl-metric-value">{{ s.sentToAgent }}</div>
            <div class="sl-metric-label">Sent to agent</div>
          </div>
          <div class="sl-metric">
            <div class="ai-score ai-score-sm" :class="scoreClass(s.avgScore)">{{ s.avgScore }}</div>
            <div class="sl-metric-label">Avg score</div>
          </div>
          <div class="sl-metric">
            <div class="sl-metric-value sl-metric-agent">{{ s.agentName }}</div>
            <div class="sl-metric-label">Manning agent</div>
          </div>
        </div>

        <div class="shortlist-progress">
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: (s.sentToAgent / s.candidateCount * 100) + '%' }"></div>
          </div>
          <span class="progress-text">{{ s.sentToAgent }}/{{ s.candidateCount }} forwarded to agent</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.shortlists-page {
  padding-top: var(--space-5);
  padding-bottom: var(--space-8);
  max-width: 900px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-5);
}
.page-header h1 { font: var(--font-h1); }
.page-sub {
  font: var(--font-small);
  color: var(--color-text-secondary);
  margin-top: 2px;
}

.shortlist-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.shortlist-card {
  padding: var(--space-4);
  cursor: pointer;
  transition: box-shadow 0.15s, border-color 0.15s;
}
.shortlist-card:hover {
  box-shadow: var(--shadow-md);
  border-color: var(--color-primary);
}

.shortlist-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-3);
}
.shortlist-info h3 { font: var(--font-h3); }
.shortlist-context {
  font: var(--font-small);
  color: var(--color-text-secondary);
  margin-top: 2px;
}

.shortlist-metrics {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-3);
  margin-bottom: var(--space-3);
}
.sl-metric { text-align: center; }
.sl-metric-value {
  font: var(--font-h2);
  color: var(--color-primary);
}
.sl-metric-agent {
  font: var(--font-small);
  font-weight: 500;
  color: var(--color-agent);
}
.sl-metric-label {
  font: var(--font-caption);
  color: var(--color-text-tertiary);
  margin-top: 2px;
}
.ai-score-sm {
  width: 32px;
  height: 32px;
  font-size: 11px;
  margin: 0 auto;
}

.shortlist-progress {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}
.progress-bar {
  flex: 1;
  height: 6px;
  background: var(--color-surface);
  border-radius: 3px;
  overflow: hidden;
}
.progress-fill {
  height: 100%;
  background: var(--color-primary);
  border-radius: 3px;
  transition: width 0.3s;
}
.progress-text {
  font: var(--font-caption);
  color: var(--color-text-tertiary);
  white-space: nowrap;
}

@media (max-width: 768px) {
  .shortlist-metrics { grid-template-columns: repeat(2, 1fr); }
  .page-header { flex-direction: column; gap: var(--space-3); }
}
</style>
