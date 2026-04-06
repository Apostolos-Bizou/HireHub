<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/services/api'
import { useShortlistStore } from '@/stores/shortlists'

const router = useRouter()
const store = useShortlistStore()
const shortlists = computed(() => store.shortlists)

// Modal state
const showModal = ref(false)
const saving = ref(false)
const saveError = ref('')
const saveSuccess = ref(false)

// Form fields
const newTitle = ref('')
const newVessel = ref('')
const newRank = ref('')
const newVesselType = ref('')

const ranks = ['Master', 'Chief Officer', '2nd Officer', '3rd Officer', 'Chief Engineer', '2nd Engineer', '3rd Engineer', '4th Engineer', 'Bosun', 'AB Seaman', 'Electrician', 'Fitter', 'Oiler', 'Cook / Steward']
const vesselTypes = ['Oil/Chemical Tanker', 'Bulk Carrier', 'Container', 'LPG/LNG', 'Passenger/Cruise', 'Offshore/AHTS', 'General Cargo']

// Auto-generate title from rank + vessel type
const autoTitle = computed(() => {
  if (newRank.value && newVesselType.value) return `${newRank.value} — ${newVesselType.value}`
  if (newRank.value) return newRank.value
  return ''
})

function openModal() {
  newTitle.value = ''
  newVessel.value = ''
  newRank.value = ''
  newVesselType.value = ''
  saveError.value = ''
  saveSuccess.value = false
  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

async function createShortlist() {
  const title = newTitle.value.trim() || autoTitle.value
  if (!title) { saveError.value = 'Please select a rank or enter a title'; return }

  saving.value = true
  saveError.value = ''
  try {
    const { data } = await api.post('/shortlists', {
      title: title,
      vesselContext: newVessel.value.trim() || null
    })
    // Add to store
    store.addShortlist({
      id: data.id || String(Date.now()),
      title: title,
      vesselContext: newVessel.value.trim() || 'New shortlist',
      status: 'ACTIVE',
      candidateCount: 0,
      sentToAgent: 0,
      avgScore: 0,
      created: new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }),
      agentName: '—'
    })
    saveSuccess.value = true
    setTimeout(() => { closeModal(); saveSuccess.value = false }, 1200)
  } catch (e) {
    // If API fails, still add locally for demo
    store.addShortlist({
      id: String(Date.now()),
      title: title,
      vesselContext: newVessel.value.trim() || 'New shortlist',
      status: 'ACTIVE',
      candidateCount: 0,
      sentToAgent: 0,
      avgScore: 0,
      created: new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }),
      agentName: '—'
    })
    saveSuccess.value = true
    setTimeout(() => { closeModal(); saveSuccess.value = false }, 1200)
  } finally {
    saving.value = false
  }
}

function scoreClass(s) { return s >= 85 ? "ai-score-high" : s >= 70 ? "ai-score-mid" : "ai-score-low" }
</script>

<template>
  <div class="container shortlists-page">
    <div class="page-header">
      <div>
        <h1>My Shortlists</h1>
        <p class="page-sub">{{ shortlists.length }} shortlists · {{ shortlists.reduce((a, s) => a + s.candidateCount, 0) }} total candidates</p>
      </div>
      <button class="btn btn-primary" @click="openModal">
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
            <div class="ai-score ai-score-sm" :class="s.avgScore ? scoreClass(s.avgScore) : ''">{{ s.avgScore || '—' }}</div>
            <div class="sl-metric-label">Avg score</div>
          </div>
          <div class="sl-metric">
            <div class="sl-metric-value sl-metric-agent">{{ s.agentName }}</div>
            <div class="sl-metric-label">Manning agent</div>
          </div>
        </div>

        <div class="shortlist-progress">
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: (s.candidateCount ? s.sentToAgent / s.candidateCount * 100 : 0) + '%' }"></div>
          </div>
          <span class="progress-text">{{ s.sentToAgent }}/{{ s.candidateCount }} forwarded to agent</span>
        </div>
      </div>
    </div>

    <!-- New Shortlist Modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-card">
        <div class="modal-header">
          <h2>Create new shortlist</h2>
          <button class="modal-close" @click="closeModal">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
          </button>
        </div>

        <div v-if="saveSuccess" class="modal-success">
          <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="#1B5E20" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
          <p>Shortlist created!</p>
        </div>

        <div v-else class="modal-body">
          <div class="form-group">
            <label>Rank *</label>
            <select v-model="newRank">
              <option value="">Select rank...</option>
              <option v-for="r in ranks" :key="r" :value="r">{{ r }}</option>
            </select>
          </div>

          <div class="form-group">
            <label>Vessel type</label>
            <select v-model="newVesselType">
              <option value="">Select vessel type...</option>
              <option v-for="v in vesselTypes" :key="v" :value="v">{{ v }}</option>
            </select>
          </div>

          <div class="form-group">
            <label>Title <span class="form-hint">(auto-generated or custom)</span></label>
            <input type="text" v-model="newTitle" :placeholder="autoTitle || 'e.g. 3rd Officer — Oil/Chemical Tanker'" />
          </div>

          <div class="form-group">
            <label>Vessel / context <span class="form-hint">(optional)</span></label>
            <input type="text" v-model="newVessel" placeholder="e.g. MT Pacific Voyager crew change" />
          </div>

          <div v-if="autoTitle && !newTitle" class="form-preview">
            <span class="form-preview-label">Will be created as:</span>
            <strong>{{ autoTitle }}</strong>
          </div>

          <div v-if="saveError" class="form-error">{{ saveError }}</div>

          <div class="modal-actions">
            <button class="btn btn-secondary" @click="closeModal">Cancel</button>
            <button class="btn btn-primary" @click="createShortlist" :disabled="saving">
              {{ saving ? 'Creating...' : 'Create shortlist' }}
            </button>
          </div>
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

/* Modal */
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

.modal-success {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-8) var(--space-5);
}
.modal-success p {
  font: var(--font-h3);
  color: var(--color-success);
}

.form-group {
  margin-bottom: var(--space-4);
}
.form-group label {
  display: block;
  font: var(--font-small);
  font-weight: 500;
  color: var(--color-text-secondary);
  margin-bottom: var(--space-1);
}
.form-hint {
  font-weight: 400;
  color: var(--color-text-tertiary);
}
.form-group select,
.form-group input {
  width: 100%;
  padding: 10px 14px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font: var(--font-body);
  color: var(--color-text-primary);
  background: var(--color-white);
  outline: none;
  transition: border-color 0.15s;
}
.form-group select:focus,
.form-group input:focus { border-color: var(--color-primary); }

.form-preview {
  background: var(--color-primary-light);
  border-radius: var(--radius-md);
  padding: var(--space-3) var(--space-4);
  margin-bottom: var(--space-4);
}
.form-preview-label {
  display: block;
  font: var(--font-caption);
  color: var(--color-text-secondary);
  margin-bottom: 4px;
}
.form-preview strong {
  font: var(--font-body);
  font-weight: 500;
  color: var(--color-primary);
}

.form-error {
  background: var(--color-danger-bg);
  color: var(--color-danger);
  padding: var(--space-2) var(--space-3);
  border-radius: var(--radius-sm);
  font: var(--font-small);
  margin-bottom: var(--space-3);
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-2);
  padding-top: var(--space-3);
  border-top: 1px solid var(--color-border);
}

@media (max-width: 768px) {
  .shortlist-metrics { grid-template-columns: repeat(2, 1fr); }
  .page-header { flex-direction: column; gap: var(--space-3); }
  .modal-card { max-width: 100%; }
}
</style>
