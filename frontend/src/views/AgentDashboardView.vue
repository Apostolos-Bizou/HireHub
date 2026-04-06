<script setup>
import { ref } from 'vue'

const requests = ref([
  { id: 1, owner: "Tsakos Energy Navigation", ownerInitials: "TE", position: "3rd Officer — Oil Tanker", vessel: "MT Athens Glory", dwt: "115,000 DWT", crewChangeDate: "May 15, 2026", candidates: 3, urgency: "Urgent", location: "Piraeus, Greece", timestamp: "2 hours ago" },
  { id: 2, owner: "Diana Shipping", ownerInitials: "DS", position: "Chief Engineer — Bulk Carrier", vessel: "MV Diana Explorer", dwt: "82,000 DWT", crewChangeDate: "Jun 1, 2026", candidates: 5, urgency: "Standard", location: "Athens, Greece", timestamp: "Yesterday" },
  { id: 3, owner: "Capital Ship Management", ownerInitials: "CS", position: "2nd Officer — Container", vessel: "MV Capital Fortune", dwt: "65,000 DWT", crewChangeDate: "May 20, 2026", candidates: 2, urgency: "Urgent", location: "Piraeus, Greece", timestamp: "3 hours ago" },
  { id: 4, owner: "Costamare Inc.", ownerInitials: "CI", position: "Bosun — Container", vessel: "MV Cosco Hellas", dwt: "48,000 DWT", crewChangeDate: "Jul 10, 2026", candidates: 4, urgency: "Standard", location: "Athens, Greece", timestamp: "2 days ago" }
])

const pipeline = ref({
  received: [
    { name: "Juan Dela Cruz", rank: "3rd Officer", owner: "Tsakos", aiScore: 92 },
    { name: "Paolo Castillo", rank: "3rd Officer", owner: "Capital Ship", aiScore: 76 },
    { name: "Lazar Stoyanov", rank: "AB", owner: "Costamare", aiScore: 68 }
  ],
  vetting: [
    { name: "Ruslan Goncharov", rank: "2nd Officer", owner: "Diana", aiScore: 87 },
    { name: "Andrei Marinescu", rank: "Chief Officer", owner: "Tsakos", aiScore: 81 },
    { name: "Dmitry Petrov", rank: "Chief Engineer", owner: "Diana", aiScore: 95 },
    { name: "Miguel Santos", rank: "2nd Engineer", owner: "Capital Ship", aiScore: 89 },
    { name: "Chen Wei", rank: "Oiler", owner: "Costamare", aiScore: 72 }
  ],
  contract: [
    { name: "Yuri Kovalenko", rank: "3rd Officer", owner: "Tsakos", aiScore: 88 },
    { name: "Radu Ionescu", rank: "AB", owner: "Diana", aiScore: 74 }
  ],
  deployment: [
    { name: "Mark Reyes", rank: "Bosun", owner: "Capital Ship", aiScore: 85 },
    { name: "Ivan Kolev", rank: "Cook", owner: "Costamare", aiScore: 79 }
  ]
})

const pipelineStages = [
  { key: 'received', label: 'Received', color: 'var(--color-primary)' },
  { key: 'vetting', label: 'Vetting', color: 'var(--color-accent)' },
  { key: 'contract', label: 'Contract', color: 'var(--color-agent)' },
  { key: 'deployment', label: 'Deployment', color: 'var(--color-success)' }
]

function scoreClass(s) { return s >= 85 ? "ai-score-high" : s >= 70 ? "ai-score-mid" : "ai-score-low" }
</script>

<template>
  <div class="container agent-layout">
    <!-- Agency Header -->
    <div class="agency-header card">
      <div class="agency-info">
        <div class="avatar avatar-lg avatar-agent">MA</div>
        <div>
          <h1 class="agency-name">Magsaysay Maritime Corp.</h1>
          <div class="agency-meta">
            <span class="badge badge-agent">DMW-1234-2026</span>
            <span>8 principals served</span>
            <span>·</span>
            <span>487 deployments YTD</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Metrics Row -->
    <div class="metrics-grid">
      <div class="card metric-box">
        <div class="metric-value metric-urgent">{{ requests.length }}</div>
        <div class="metric-label">New requests</div>
        <div class="metric-trend">+2 this week</div>
      </div>
      <div class="card metric-box">
        <div class="metric-value">8</div>
        <div class="metric-label">Active pipelines</div>
        <div class="metric-trend">3 urgent</div>
      </div>
      <div class="card metric-box">
        <div class="metric-value">15</div>
        <div class="metric-label">In review</div>
        <div class="metric-trend">5 ready for contract</div>
      </div>
      <div class="card metric-box">
        <div class="metric-value metric-success">94%</div>
        <div class="metric-label">Placement rate</div>
        <div class="metric-trend">↑ 3% vs last quarter</div>
      </div>
    </div>

    <!-- Incoming Shortlists -->
    <div class="section-header">
      <h2>
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
        Incoming Shortlists
      </h2>
      <span class="section-count">{{ requests.length }} pending</span>
    </div>

    <div class="shortlist-feed">
      <div v-for="r in requests" :key="r.id" class="card request-card">
        <div class="request-top">
          <div class="request-owner">
            <div class="avatar avatar-sm avatar-owner">{{ r.ownerInitials }}</div>
            <div>
              <strong>{{ r.owner }}</strong>
              <span class="request-time">{{ r.timestamp }}</span>
            </div>
          </div>
          <div class="request-badges">
            <span class="badge" :class="r.urgency === 'Urgent' ? 'badge-danger' : 'badge-info'">{{ r.urgency }}</span>
            <span class="badge badge-info">{{ r.candidates }} candidates</span>
          </div>
        </div>
        <div class="request-details">
          <div class="request-position">{{ r.position }}</div>
          <div class="request-vessel">{{ r.vessel }} · {{ r.dwt }} · Crew change: {{ r.crewChangeDate }}</div>
          <div class="request-location">{{ r.location }}</div>
        </div>
        <div class="request-actions">
          <button class="btn btn-primary btn-sm">Review candidates</button>
          <button class="btn btn-secondary btn-sm">Message owner</button>
          <button class="btn btn-tertiary btn-sm">Decline</button>
        </div>
      </div>
    </div>

    <!-- Deployment Pipeline -->
    <div class="section-header">
      <h2>
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><line x1="3" y1="9" x2="21" y2="9"/><line x1="9" y1="21" x2="9" y2="9"/></svg>
        Deployment Pipeline
      </h2>
    </div>

    <div class="pipeline-grid">
      <div v-for="stage in pipelineStages" :key="stage.key" class="pipeline-column">
        <div class="pipeline-header" :style="{ borderTopColor: stage.color }">
          <span class="pipeline-stage-name">{{ stage.label }}</span>
          <span class="pipeline-stage-count">{{ pipeline[stage.key].length }}</span>
        </div>
        <div class="pipeline-cards">
          <div v-for="c in pipeline[stage.key]" :key="c.name" class="pipeline-card">
            <div class="pipeline-card-top">
              <div class="avatar avatar-sm avatar-seafarer">{{ c.name.split(' ').map(w=>w[0]).join('').substring(0,2) }}</div>
              <div class="pipeline-card-info">
                <strong>{{ c.name }}</strong>
                <span>{{ c.rank }}</span>
              </div>
              <div class="ai-score ai-score-sm" :class="scoreClass(c.aiScore)">{{ c.aiScore }}</div>
            </div>
            <div class="pipeline-card-owner">{{ c.owner }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.agent-layout {
  padding-top: var(--space-5);
  padding-bottom: var(--space-8);
  max-width: 1128px;
}

/* --- Agency Header --- */
.agency-header {
  padding: var(--space-5);
  margin-bottom: var(--space-4);
}
.agency-info {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}
.agency-name {
  font: var(--font-h1);
}
.agency-meta {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  margin-top: var(--space-1);
  font: var(--font-small);
  color: var(--color-text-secondary);
}

/* --- Metrics --- */
.metrics-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-3);
  margin-bottom: var(--space-5);
}
.metric-box {
  padding: var(--space-4);
  text-align: center;
}
.metric-value {
  font: var(--font-h1);
  font-size: 28px;
  color: var(--color-primary);
}
.metric-urgent { color: var(--color-danger); }
.metric-success { color: var(--color-success); }
.metric-label {
  font: var(--font-small);
  color: var(--color-text-secondary);
  margin-top: var(--space-1);
}
.metric-trend {
  font: var(--font-caption);
  color: var(--color-text-tertiary);
  margin-top: var(--space-1);
}

/* --- Section Header --- */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-3);
}
.section-header h2 {
  font: var(--font-h2);
  display: flex;
  align-items: center;
  gap: var(--space-2);
}
.section-count {
  font: var(--font-small);
  color: var(--color-text-secondary);
}

/* --- Shortlist Feed --- */
.shortlist-feed {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
  margin-bottom: var(--space-5);
}
.request-card {
  padding: var(--space-4);
}
.request-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-3);
}
.request-owner {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}
.request-owner strong {
  font: var(--font-body);
  font-weight: 600;
  display: block;
}
.request-time {
  font: var(--font-caption);
  color: var(--color-text-tertiary);
  display: block;
}
.request-badges {
  display: flex;
  gap: var(--space-2);
}
.request-details {
  margin-bottom: var(--space-3);
}
.request-position {
  font: var(--font-h3);
  margin-bottom: 2px;
}
.request-vessel {
  font: var(--font-small);
  color: var(--color-text-secondary);
}
.request-location {
  font: var(--font-caption);
  color: var(--color-text-tertiary);
  margin-top: 2px;
}
.request-actions {
  display: flex;
  gap: var(--space-2);
}
.btn-sm {
  font-size: 12px;
  padding: 6px 14px;
}

/* --- Pipeline --- */
.pipeline-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-3);
}
.pipeline-column {
  min-height: 200px;
}
.pipeline-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-2) var(--space-3);
  background: var(--color-surface);
  border-radius: var(--radius-md) var(--radius-md) 0 0;
  border-top: 3px solid var(--color-primary);
}
.pipeline-stage-name {
  font: var(--font-small);
  font-weight: 600;
  color: var(--color-text-primary);
}
.pipeline-stage-count {
  font: var(--font-caption);
  font-weight: 600;
  background: var(--color-white);
  color: var(--color-text-secondary);
  padding: 2px 8px;
  border-radius: 10px;
}
.pipeline-cards {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
  padding-top: var(--space-2);
}
.pipeline-card {
  background: var(--color-white);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  padding: var(--space-3);
}
.pipeline-card-top {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}
.pipeline-card-info {
  flex: 1;
  min-width: 0;
}
.pipeline-card-info strong {
  font: var(--font-small);
  font-weight: 600;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.pipeline-card-info span {
  font: var(--font-caption);
  color: var(--color-text-secondary);
}
.pipeline-card-owner {
  font: var(--font-caption);
  color: var(--color-text-tertiary);
  margin-top: var(--space-1);
  padding-left: 36px;
}
.ai-score-sm {
  width: 32px;
  height: 32px;
  font-size: 11px;
}

/* --- Responsive --- */
@media (max-width: 1024px) {
  .pipeline-grid { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 768px) {
  .metrics-grid { grid-template-columns: repeat(2, 1fr); }
  .pipeline-grid { grid-template-columns: 1fr; }
  .request-top { flex-direction: column; gap: var(--space-2); }
  .request-actions { flex-wrap: wrap; }
}
</style>
