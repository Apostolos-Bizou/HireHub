<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const activeTab = ref(0)

const candidates = ref([
  { id: 1, name: "Juan Dela Cruz", rank: "3rd Officer", nationality: "Filipino", years: 4, verified: true, aiScore: 92, status: "SHORTLISTED", availability: "May 2026" },
  { id: 2, name: "Ruslan Goncharov", rank: "3rd Officer", nationality: "Ukrainian", years: 6, verified: true, aiScore: 87, status: "SENT_TO_AGENT", availability: "Jun 2026" },
  { id: 3, name: "Andrei Marinescu", rank: "3rd Officer", nationality: "Romanian", years: 3, verified: true, aiScore: 81, status: "UNDER_REVIEW", availability: "Apr 2026" },
  { id: 4, name: "Paolo Castillo", rank: "3rd Officer", nationality: "Filipino", years: 5, verified: false, aiScore: 76, status: "SENT_TO_AGENT", availability: "Jul 2026" },
  { id: 5, name: "Lazar Stoyanov", rank: "3rd Officer", nationality: "Bulgarian", years: 2, verified: false, aiScore: 68, status: "SHORTLISTED", availability: "May 2026" }
])

const selected = ref(new Set())

const tabs = computed(() => [
  { label: 'All', count: candidates.value.length },
  { label: 'Shortlisted', count: candidates.value.filter(c => c.status === 'SHORTLISTED').length },
  { label: 'Under review', count: candidates.value.filter(c => c.status === 'UNDER_REVIEW').length },
  { label: 'Sent to agent', count: candidates.value.filter(c => c.status === 'SENT_TO_AGENT').length }
])

const filteredCandidates = computed(() => {
  if (activeTab.value === 0) return candidates.value
  const statusMap = ['ALL', 'SHORTLISTED', 'UNDER_REVIEW', 'SENT_TO_AGENT']
  return candidates.value.filter(c => c.status === statusMap[activeTab.value])
})

const avgScore = computed(() => {
  const scores = candidates.value.map(c => c.aiScore)
  return Math.round(scores.reduce((a, b) => a + b, 0) / scores.length)
})

function scoreClass(s) { return s >= 85 ? "ai-score-high" : s >= 70 ? "ai-score-mid" : "ai-score-low" }

function statusBadge(s) {
  if (s === 'SENT_TO_AGENT') return 'badge-verified'
  if (s === 'UNDER_REVIEW') return 'badge-warning'
  return 'badge-info'
}

function statusLabel(s) {
  return s.replace(/_/g, ' ').toLowerCase().replace(/^\w/, c => c.toUpperCase())
}

function toggleAll() {
  if (selected.value.size === filteredCandidates.value.length) selected.value.clear()
  else filteredCandidates.value.forEach(c => selected.value.add(c.id))
}
</script>

<template>
  <div class="container shortlist-layout">
    <!-- Header -->
    <div class="shortlist-header">
      <div class="header-left">
        <button class="btn-back" @click="router.push('/shortlists')">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="15 18 9 12 15 6"/></svg>
          Back
        </button>
        <h1>3rd Officer — Oil/Chemical Tanker</h1>
        <p class="header-context">MT Pacific Voyager replacement · Created Apr 2, 2026</p>
      </div>
      <div class="header-actions">
        <button class="btn btn-secondary">Compare</button>
        <button class="btn btn-primary">
          <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="22" y1="2" x2="11" y2="13"/><polygon points="22 2 15 22 11 13 2 9 22 2"/></svg>
          Forward to Agent
        </button>
      </div>
    </div>

    <!-- Pipeline Tabs -->
    <div class="card pipeline-card">
      <div class="pipeline-tabs">
        <button
          v-for="(t, i) in tabs"
          :key="i"
          @click="activeTab = i"
          class="pipeline-tab"
          :class="{ active: activeTab === i }"
        >
          {{ t.label }}
          <span class="tab-count" :class="{ 'tab-count-active': activeTab === i }">{{ t.count }}</span>
        </button>
      </div>

      <!-- Select All -->
      <div class="select-all-row">
        <label class="checkbox-label">
          <input type="checkbox" :checked="selected.size === filteredCandidates.length && filteredCandidates.length > 0" @change="toggleAll" />
          <span>Select all</span>
        </label>
        <span v-if="selected.size > 0" class="selected-count">{{ selected.size }} selected</span>
      </div>

      <!-- Candidate Rows -->
      <div class="candidate-rows">
        <div
          v-for="c in filteredCandidates"
          :key="c.id"
          class="candidate-row"
          :class="{ 'row-selected': selected.has(c.id) }"
        >
          <input type="checkbox" :checked="selected.has(c.id)" @change="selected.has(c.id) ? selected.delete(c.id) : selected.add(c.id)" />
          <div class="avatar avatar-md avatar-seafarer">{{ c.name.split(' ').map(w => w[0]).join('').substring(0,2) }}</div>
          <div class="row-info">
            <div class="row-name">
              {{ c.name }}
              <span v-if="c.verified" class="badge badge-verified" style="margin-left:4px">
                <svg xmlns="http://www.w3.org/2000/svg" width="9" height="9" viewBox="0 0 24 24" fill="currentColor"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/></svg>
                Verified
              </span>
            </div>
            <p class="row-meta">{{ c.rank }} · {{ c.nationality }} · {{ c.years }} yrs · Available {{ c.availability }}</p>
          </div>
          <div class="ai-score" :class="scoreClass(c.aiScore)">{{ c.aiScore }}</div>
          <span class="badge" :class="statusBadge(c.status)">{{ statusLabel(c.status) }}</span>
          <div class="row-actions">
            <button class="btn-icon" title="View profile" @click="router.push(`/profile/${c.id}`)">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
            </button>
            <button class="btn-icon" title="Message">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
            </button>
            <button class="btn-icon btn-icon-danger" title="Remove">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Agent Integration Panel -->
    <div class="card agent-panel">
      <div class="agent-panel-header">
        <div class="agent-panel-info">
          <div class="avatar avatar-md avatar-agent">MA</div>
          <div>
            <h3>Magsaysay Maritime Corp.</h3>
            <div class="agent-panel-meta">
              <span class="badge badge-agent">DMW-1234-2026</span>
              <span>Manila, Philippines</span>
            </div>
          </div>
        </div>
        <button class="btn btn-tertiary btn-sm">Change agent</button>
      </div>
      <div class="agent-metrics">
        <div class="metric-card">
          <div class="value">{{ candidates.length }}</div>
          <div class="label">Total candidates</div>
        </div>
        <div class="metric-card">
          <div class="value">{{ candidates.filter(c => c.status === 'SENT_TO_AGENT').length }}</div>
          <div class="label">Sent to agent</div>
        </div>
        <div class="metric-card">
          <div class="value">{{ candidates.filter(c => c.status === 'UNDER_REVIEW').length }}</div>
          <div class="label">Under review</div>
        </div>
        <div class="metric-card">
          <div class="value">{{ avgScore }}</div>
          <div class="label">Avg AI score</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.shortlist-layout {
  padding-top: var(--space-5);
  padding-bottom: var(--space-8);
  max-width: 960px;
}

/* --- Header --- */
.shortlist-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-4);
  gap: var(--space-4);
}
.header-left h1 {
  font: var(--font-h1);
  margin-top: var(--space-1);
}
.header-context {
  font: var(--font-small);
  color: var(--color-text-secondary);
  margin-top: 2px;
}
.header-actions {
  display: flex;
  gap: var(--space-2);
  flex-shrink: 0;
}
.btn-back {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  background: none;
  border: none;
  font: var(--font-small);
  color: var(--color-text-secondary);
  cursor: pointer;
  padding: 0;
}
.btn-back:hover { color: var(--color-primary); }

/* --- Pipeline Tabs --- */
.pipeline-card {
  margin-bottom: var(--space-3);
}
.pipeline-tabs {
  display: flex;
  border-bottom: 1px solid var(--color-border);
}
.pipeline-tab {
  flex: 1;
  padding: var(--space-3) var(--space-4);
  background: none;
  border: none;
  font: var(--font-small);
  font-weight: 500;
  color: var(--color-text-secondary);
  cursor: pointer;
  border-bottom: 2px solid transparent;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
  transition: color 0.15s;
}
.pipeline-tab:hover { background: var(--color-surface); }
.pipeline-tab.active {
  color: var(--color-primary);
  border-bottom-color: var(--color-primary);
}
.tab-count {
  font: var(--font-caption);
  font-weight: 600;
  background: var(--color-surface);
  color: var(--color-text-secondary);
  padding: 1px 7px;
  border-radius: 10px;
}
.tab-count-active {
  background: var(--color-primary-light);
  color: var(--color-primary);
}

/* --- Select All --- */
.select-all-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-2) var(--space-4);
  border-bottom: 1px solid var(--color-border);
  background: var(--color-surface);
}
.checkbox-label {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font: var(--font-caption);
  font-weight: 500;
  color: var(--color-text-secondary);
  cursor: pointer;
}
.checkbox-label input {
  width: 15px;
  height: 15px;
  accent-color: var(--color-primary);
  cursor: pointer;
}
.selected-count {
  font: var(--font-caption);
  font-weight: 600;
  color: var(--color-primary);
  background: var(--color-primary-light);
  padding: 2px 8px;
  border-radius: 10px;
}

/* --- Candidate Rows --- */
.candidate-rows {
  /* no extra style needed */
}
.candidate-row {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3) var(--space-4);
  border-bottom: 1px solid var(--color-border);
  transition: background 0.1s;
}
.candidate-row:last-child { border-bottom: none; }
.candidate-row:hover { background: rgba(0,0,0,0.015); }
.row-selected { background: rgba(10, 102, 194, 0.04); }
.candidate-row input[type="checkbox"] {
  width: 16px;
  height: 16px;
  accent-color: var(--color-primary);
  cursor: pointer;
  flex-shrink: 0;
}
.row-info {
  flex: 1;
  min-width: 0;
}
.row-name {
  font: var(--font-body);
  font-weight: 500;
  display: flex;
  align-items: center;
}
.row-meta {
  font: var(--font-small);
  color: var(--color-text-secondary);
  margin-top: 1px;
}
.row-actions {
  display: flex;
  gap: 4px;
  flex-shrink: 0;
}
.btn-icon {
  width: 30px;
  height: 30px;
  border: none;
  background: none;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: var(--color-text-secondary);
  transition: background 0.15s, color 0.15s;
}
.btn-icon:hover { background: var(--color-surface); color: var(--color-primary); }
.btn-icon-danger:hover { background: var(--color-danger-bg); color: var(--color-danger); }

/* --- Agent Panel --- */
.agent-panel {
  padding: var(--space-4);
}
.agent-panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-3);
}
.agent-panel-info {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}
.agent-panel-info h3 {
  font: var(--font-h3);
}
.agent-panel-meta {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  margin-top: 2px;
  font: var(--font-small);
  color: var(--color-text-secondary);
}
.agent-metrics {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-3);
}
.btn-sm {
  font-size: 12px;
  padding: 6px 14px;
}

/* --- Responsive --- */
@media (max-width: 768px) {
  .shortlist-header { flex-direction: column; }
  .header-actions { width: 100%; }
  .candidate-row { flex-wrap: wrap; gap: var(--space-2); }
  .row-actions { width: 100%; justify-content: flex-end; }
  .agent-metrics { grid-template-columns: repeat(2, 1fr); }
  .pipeline-tab { padding: var(--space-2); font-size: 11px; }
}
</style>
