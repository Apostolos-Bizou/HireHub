<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/services/api'

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

// Agent state
const agents = [
  { id: 'magsaysay', name: 'Magsaysay Maritime Corp.', dmw: 'DMW-1234-2026', location: 'Manila, Philippines', initials: 'MA', color: '#1D9E75' },
  { id: 'cfsharp', name: 'CF Sharp Crew Management', dmw: 'DMW-0891-2026', location: 'Manila, Philippines', initials: 'CF', color: '#0A66C2' },
  { id: 'bernhard', name: 'Bernhard Schulte SM', dmw: 'DMW-0456-2025', location: 'Manila, Philippines', initials: 'BS', color: '#534AB7' },
  { id: 'osa', name: 'OSA Maritime Consultancy', dmw: 'DMW-0723-2026', location: 'Manila, Philippines', initials: 'OS', color: '#E7A33E' },
  { id: 'pacific', name: 'Pacific Ace Corp.', dmw: 'DMW-0312-2025', location: 'Manila, Philippines', initials: 'PA', color: '#D85A30' }
]
const currentAgent = ref(agents[0])
const showAgentModal = ref(false)
const selectedAgentId = ref(agents[0].id)

// Forward modal
const showForwardModal = ref(false)
const forwarding = ref(false)
const forwardSuccess = ref(false)

function openAgentModal() {
  selectedAgentId.value = currentAgent.value.id
  showAgentModal.value = true
}

function changeAgent() {
  const agent = agents.find(a => a.id === selectedAgentId.value)
  if (agent) currentAgent.value = agent
  showAgentModal.value = false
}

function openForwardModal() {
  if (selected.value.size === 0) {
    // Select all shortlisted candidates if none selected
    candidates.value.filter(c => c.status === 'SHORTLISTED').forEach(c => selected.value.add(c.id))
  }
  if (selected.value.size === 0) return
  showForwardModal.value = true
}

async function forwardToAgent() {
  forwarding.value = true
  try {
    await api.post(`/shortlists/${route.params.id}/forward`, null, { params: { agentId: currentAgent.value.id } })
  } catch { /* demo mode */ }
  // Update candidate statuses locally
  candidates.value.forEach(c => {
    if (selected.value.has(c.id) && c.status !== 'SENT_TO_AGENT') {
      c.status = 'SENT_TO_AGENT'
    }
  })
  selected.value.clear()
  forwarding.value = false
  forwardSuccess.value = true
  setTimeout(() => { showForwardModal.value = false; forwardSuccess.value = false }, 1500)
}

function removeCand(id) {
  candidates.value = candidates.value.filter(c => c.id !== id)
  selected.value.delete(id)
}

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
  return scores.length ? Math.round(scores.reduce((a, b) => a + b, 0) / scores.length) : 0
})

const forwardCandidates = computed(() => candidates.value.filter(c => selected.value.has(c.id)))

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
        <button class="btn btn-primary" @click="openForwardModal">
          <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="22" y1="2" x2="11" y2="13"/><polygon points="22 2 15 22 11 13 2 9 22 2"/></svg>
          Forward to Agent{{ selected.size ? ` (${selected.size})` : '' }}
        </button>
      </div>
    </div>

    <!-- Agent Integration Panel -->
    <div class="card agent-panel">
      <div class="agent-panel-header">
        <div class="agent-panel-info">
          <div class="avatar avatar-md avatar-agent" :style="{background: currentAgent.color}">{{ currentAgent.initials }}</div>
          <div>
            <h3>{{ currentAgent.name }}</h3>
            <div class="agent-panel-meta">
              <span class="badge badge-agent">{{ currentAgent.dmw }}</span>
              <span>{{ currentAgent.location }}</span>
            </div>
          </div>
        </div>
        <button class="btn btn-tertiary btn-sm" @click="openAgentModal">Change agent</button>
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
            <button class="btn-icon btn-icon-danger" title="Remove" @click.stop="removeCand(c.id)">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Change Agent Modal -->
    <div v-if="showAgentModal" class="modal-overlay" @click.self="showAgentModal = false">
      <div class="modal-card">
        <div class="modal-header">
          <h2>Change manning agent</h2>
          <button class="modal-close" @click="showAgentModal = false">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
          </button>
        </div>
        <div class="modal-body">
          <p class="modal-desc">Select the DMW-licensed manning agent for this shortlist. The agent will receive all forwarded candidates.</p>
          <div class="agent-options">
            <label v-for="a in agents" :key="a.id" class="agent-option" :class="{ 'agent-option-selected': selectedAgentId === a.id }">
              <input type="radio" :value="a.id" v-model="selectedAgentId" />
              <div class="avatar avatar-sm" :style="{ background: a.color, color: 'white', width: '36px', height: '36px', borderRadius: '8px', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 600, fontSize: '12px', flexShrink: 0 }">{{ a.initials }}</div>
              <div class="agent-option-info">
                <strong>{{ a.name }}</strong>
                <span>{{ a.dmw }} · {{ a.location }}</span>
              </div>
            </label>
          </div>
          <div class="modal-actions">
            <button class="btn btn-secondary" @click="showAgentModal = false">Cancel</button>
            <button class="btn btn-primary" @click="changeAgent">Confirm agent</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Forward to Agent Modal -->
    <div v-if="showForwardModal" class="modal-overlay" @click.self="showForwardModal = false">
      <div class="modal-card">
        <div v-if="forwardSuccess" class="modal-success">
          <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="#1B5E20" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
          <p style="font:var(--font-h3);color:var(--color-success);">Forwarded to {{ currentAgent.name }}!</p>
        </div>
        <template v-else>
          <div class="modal-header">
            <h2>Forward to agent</h2>
            <button class="modal-close" @click="showForwardModal = false">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
            </button>
          </div>
          <div class="modal-body">
            <div class="forward-agent-info">
              <div class="avatar avatar-sm" :style="{ background: currentAgent.color, color: 'white', width: '36px', height: '36px', borderRadius: '8px', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 600, fontSize: '12px' }">{{ currentAgent.initials }}</div>
              <div><strong>{{ currentAgent.name }}</strong><br/><span style="font:var(--font-caption);color:var(--color-text-secondary);">{{ currentAgent.dmw }}</span></div>
            </div>
            <p class="modal-desc">The following {{ forwardCandidates.length }} candidate(s) will be forwarded:</p>
            <div class="forward-list">
              <div v-for="c in forwardCandidates" :key="c.id" class="forward-cand">
                <div class="avatar avatar-sm avatar-seafarer" style="width:28px;height:28px;font-size:10px;">{{ c.name.split(' ').map(w => w[0]).join('').substring(0,2) }}</div>
                <span>{{ c.name }}</span>
                <span class="forward-score" :class="scoreClass(c.aiScore)">{{ c.aiScore }}</span>
              </div>
            </div>
            <div class="modal-actions">
              <button class="btn btn-secondary" @click="showForwardModal = false">Cancel</button>
              <button class="btn btn-primary" @click="forwardToAgent" :disabled="forwarding">
                {{ forwarding ? 'Sending...' : 'Forward now' }}
              </button>
            </div>
          </div>
        </template>
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
  .modal-card { max-width: 100%; }
}

/* --- Modals --- */
.modal-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 200;
  padding: var(--space-4);
}
.modal-card {
  background: var(--color-white);
  border-radius: var(--radius-lg);
  width: 100%;
  max-width: 520px;
  box-shadow: var(--shadow-lg);
  overflow: hidden;
}
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-5) var(--space-5) 0;
}
.modal-header h2 { font: var(--font-h2); }
.modal-close {
  background: none;
  border: none;
  cursor: pointer;
  color: var(--color-text-tertiary);
  padding: var(--space-1);
  border-radius: var(--radius-sm);
}
.modal-close:hover { background: var(--color-surface); color: var(--color-text-primary); }
.modal-body { padding: var(--space-5); }
.modal-desc { font: var(--font-small); color: var(--color-text-secondary); margin-bottom: var(--space-4); }
.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-2);
  padding-top: var(--space-3);
  border-top: 1px solid var(--color-border);
}
.modal-success {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-8) var(--space-5);
}

/* Agent options */
.agent-options { display: flex; flex-direction: column; gap: var(--space-2); margin-bottom: var(--space-4); }
.agent-option {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3);
  border: 1.5px solid var(--color-border);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: border-color 0.15s, background 0.15s;
}
.agent-option:hover { border-color: var(--color-primary); background: var(--color-primary-light); }
.agent-option-selected { border-color: var(--color-primary); background: var(--color-primary-light); }
.agent-option input[type="radio"] { display: none; }
.agent-option-info { flex: 1; }
.agent-option-info strong { font: var(--font-body); font-weight: 500; display: block; }
.agent-option-info span { font: var(--font-caption); color: var(--color-text-secondary); }

/* Forward list */
.forward-agent-info {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3);
  background: var(--color-agent-bg);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-4);
}
.forward-list { display: flex; flex-direction: column; gap: var(--space-2); margin-bottom: var(--space-4); }
.forward-cand {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-3);
  background: var(--color-surface);
  border-radius: var(--radius-sm);
  font: var(--font-small);
}
.forward-cand span:first-of-type { flex: 1; font-weight: 500; }
.forward-score {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  border: 2px solid;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  font-weight: 600;
}
</style>
