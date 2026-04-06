<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const filters = ref({
  rank: '3rd Officer',
  vesselType: 'OIL_TANKER',
  nationality: '',
  minCrewScore: 70,
  availability: 30
})

const searching = ref(false)
const hasSearched = ref(true)

const ranks = ["3rd Officer","2nd Officer","Chief Officer","Master","3rd Engineer","2nd Engineer","Chief Engineer","Bosun","AB"]
const vesselTypes = ["OIL_TANKER","CHEMICAL_TANKER","BULK_CARRIER","CONTAINER","LPG_CARRIER","LNG_CARRIER"]
const nationalities = ["","Filipino","Ukrainian","Romanian","Bulgarian","Indian","Greek","Russian"]

const results = ref([
  { id: 101, rank: 1, name: "Juan Dela Cruz", initials: "JD", nationality: "Filipino", years: 4, availableFrom: "May 2026", reviews: 14, aiScore: 92, crewScore: 87, color: "#0A66C2", topSkill: "Zero incidents · 3 Captain endorsements" },
  { id: 106, rank: 2, name: "Dmitry Petrov", initials: "DP", nationality: "Russian", years: 12, availableFrom: "Apr 2026", reviews: 22, aiScore: 95, crewScore: 91, color: "#E7A33E", topSkill: "Chief Engineer · Exceptional safety record" },
  { id: 107, rank: 3, name: "Miguel Santos", initials: "MS", nationality: "Filipino", years: 7, availableFrom: "May 2026", reviews: 9, aiScore: 89, crewScore: 84, color: "#1D9E75", topSkill: "Chemical tanker specialist · Strong teamwork" },
  { id: 102, rank: 4, name: "Ruslan Goncharov", initials: "RG", nationality: "Ukrainian", years: 6, availableFrom: "Jun 2026", reviews: 11, aiScore: 87, crewScore: 82, color: "#0A66C2", topSkill: "ECDIS expert · Reliable under pressure" },
  { id: 103, rank: 5, name: "Andrei Marinescu", initials: "AM", nationality: "Romanian", years: 8, availableFrom: "Apr 2026", reviews: 16, aiScore: 81, crewScore: 88, color: "#534AB7", topSkill: "Highest peer rating · Leadership excellence" }
])

const shortlisted = ref(new Set())

function scoreClass(s) { return s >= 85 ? 'score-high' : s >= 70 ? 'score-mid' : 'score-low' }

function toggleShortlist(id) {
  if (shortlisted.value.has(id)) shortlisted.value.delete(id)
  else shortlisted.value.add(id)
}

function searchCandidates() {
  searching.value = true
  setTimeout(() => {
    searching.value = false
    hasSearched.value = true
  }, 800)
}
</script>

<template>
  <div class="recommender">
    <!-- Banner -->
    <div class="rec-banner">
      <div class="rec-banner-icon">
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2a4 4 0 0 1 4 4c0 1.95-1.4 3.58-3.25 3.93L12 22"/><path d="M12 2a4 4 0 0 0-4 4c0 1.95 1.4 3.58 3.25 3.93"/><circle cx="12" cy="6" r="1.5"/></svg>
      </div>
      <div>
        <strong>AI crew recommender</strong>
        <span>Set your requirements and AI finds the best candidates ranked by AI match + CrewScore.</span>
      </div>
    </div>

    <!-- Filters -->
    <div class="rec-filters card">
      <div class="rec-filter-grid">
        <div class="rec-filter-group">
          <label>Rank needed</label>
          <select v-model="filters.rank">
            <option v-for="r in ranks" :key="r" :value="r">{{ r }}</option>
          </select>
        </div>
        <div class="rec-filter-group">
          <label>Vessel type</label>
          <select v-model="filters.vesselType">
            <option v-for="v in vesselTypes" :key="v" :value="v">{{ v.replace(/_/g, ' ') }}</option>
          </select>
        </div>
        <div class="rec-filter-group">
          <label>Nationality</label>
          <select v-model="filters.nationality">
            <option v-for="n in nationalities" :key="n" :value="n">{{ n || 'Any nationality' }}</option>
          </select>
        </div>
        <div class="rec-filter-group">
          <label>Min CrewScore</label>
          <select v-model="filters.minCrewScore">
            <option :value="0">Any</option>
            <option :value="60">60+</option>
            <option :value="70">70+</option>
            <option :value="80">80+</option>
            <option :value="90">90+</option>
          </select>
        </div>
        <div class="rec-filter-group">
          <label>Available within</label>
          <select v-model="filters.availability">
            <option :value="30">30 days</option>
            <option :value="60">60 days</option>
            <option :value="90">90 days</option>
            <option :value="0">Any time</option>
          </select>
        </div>
        <div class="rec-filter-group rec-filter-btn-wrap">
          <button class="btn btn-primary rec-search-btn" @click="searchCandidates" :disabled="searching">
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2a4 4 0 0 1 4 4c0 1.95-1.4 3.58-3.25 3.93L12 22"/><path d="M12 2a4 4 0 0 0-4 4c0 1.95 1.4 3.58 3.25 3.93"/><circle cx="12" cy="6" r="1.5"/></svg>
            {{ searching ? 'Searching...' : 'Find candidates' }}
          </button>
        </div>
      </div>
    </div>

    <!-- Results -->
    <div v-if="searching" class="rec-loading">
      <div class="rec-spinner"></div>
      <span>AI is analyzing candidates...</span>
    </div>

    <div v-if="hasSearched && !searching">
      <div class="rec-results-header">
        <span>Top {{ results.length }} matches for <strong>{{ filters.rank }} · {{ filters.vesselType.replace(/_/g, ' ') }} · CrewScore {{ filters.minCrewScore }}+</strong></span>
        <div class="rec-legend">
          <span class="rec-legend-item"><span class="rec-legend-dot rec-legend-ai"></span> AI match</span>
          <span class="rec-legend-item"><span class="rec-legend-dot rec-legend-crew"></span> CrewScore</span>
        </div>
      </div>

      <div v-for="c in results" :key="c.id" class="rec-card card">
        <div class="rec-rank-num">#{{ c.rank }}</div>
        <div class="rec-avatar" :style="{ background: c.color }">{{ c.initials }}</div>
        <div class="rec-info" @click="router.push(`/seafarer/${c.id}`)">
          <div class="rec-name">{{ c.name }}</div>
          <div class="rec-detail">{{ c.nationality }} · {{ c.years }} yrs · Available {{ c.availableFrom }} · {{ c.reviews }} peer reviews</div>
          <div class="rec-skill">{{ c.topSkill }}</div>
        </div>
        <div class="rec-scores">
          <div class="rec-score-ring" :class="scoreClass(c.aiScore)" title="AI Match Score">
            <span class="rec-score-num">{{ c.aiScore }}</span>
            <span class="rec-score-label">AI</span>
          </div>
          <div class="rec-score-ring rec-score-crew" :class="'crew-' + scoreClass(c.crewScore)" title="CrewScore">
            <span class="rec-score-num">{{ c.crewScore }}</span>
            <span class="rec-score-label">Crew</span>
          </div>
        </div>
        <button
          class="btn rec-shortlist-btn"
          :class="shortlisted.has(c.id) ? 'btn-shortlisted' : 'btn-primary'"
          @click="toggleShortlist(c.id)"
        >
          {{ shortlisted.has(c.id) ? '✓ Added' : '+ Shortlist' }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.recommender { margin-bottom: var(--space-4); }

.rec-banner {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3) var(--space-4);
  background: var(--color-primary-light);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-3);
}
.rec-banner-icon {
  width: 36px;
  height: 36px;
  background: var(--color-primary);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}
.rec-banner strong { display: block; font: var(--font-small); font-weight: 600; color: var(--color-primary-dark); }
.rec-banner span { font: var(--font-caption); color: var(--color-text-secondary); }

.rec-filters { padding: var(--space-4); margin-bottom: var(--space-3); }
.rec-filter-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--space-3);
}
.rec-filter-group label {
  display: block;
  font: var(--font-caption);
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  color: var(--color-text-tertiary);
  margin-bottom: 4px;
}
.rec-filter-group select {
  width: 100%;
  padding: var(--space-2) var(--space-3);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font: var(--font-small);
  background: var(--color-white);
}
.rec-filter-btn-wrap {
  display: flex;
  align-items: flex-end;
}
.rec-search-btn {
  width: 100%;
  padding: var(--space-2) var(--space-4);
}

.rec-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-8);
  color: var(--color-text-secondary);
  font: var(--font-body);
}
.rec-spinner {
  width: 32px;
  height: 32px;
  border: 3px solid var(--color-border);
  border-top-color: var(--color-primary);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

.rec-results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-3);
  font: var(--font-small);
  color: var(--color-text-secondary);
}
.rec-legend { display: flex; gap: var(--space-3); }
.rec-legend-item { display: flex; align-items: center; gap: 4px; font: var(--font-caption); color: var(--color-text-tertiary); }
.rec-legend-dot { width: 10px; height: 10px; border-radius: 50%; }
.rec-legend-ai { border: 2px solid var(--color-primary); }
.rec-legend-crew { border: 2px solid var(--color-success); }

.rec-card {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-4);
  margin-bottom: var(--space-2);
  transition: box-shadow 0.15s;
}
.rec-card:hover { box-shadow: var(--shadow-md); }
.rec-rank-num {
  font: var(--font-h2);
  color: var(--color-primary);
  width: 28px;
  text-align: center;
  flex-shrink: 0;
}
.rec-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 500;
  flex-shrink: 0;
}
.rec-info { flex: 1; min-width: 0; cursor: pointer; }
.rec-name { font: var(--font-body); font-weight: 600; }
.rec-detail { font: var(--font-small); color: var(--color-text-secondary); margin-top: 1px; }
.rec-skill { font: var(--font-caption); color: var(--color-primary); margin-top: 2px; font-weight: 500; }

.rec-scores { display: flex; gap: var(--space-2); flex-shrink: 0; }
.rec-score-ring {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2.5px solid;
}
.rec-score-num { font-size: 13px; font-weight: 600; line-height: 1; }
.rec-score-label { font-size: 8px; text-transform: uppercase; letter-spacing: 0.5px; }

.score-high { border-color: var(--color-primary); color: var(--color-primary); }
.score-mid { border-color: var(--color-accent); color: var(--color-accent); }
.score-low { border-color: var(--color-text-tertiary); color: var(--color-text-tertiary); }
.crew-score-high { border-color: var(--color-success); color: var(--color-success); }
.crew-score-mid { border-color: var(--color-agent); color: var(--color-agent); }
.crew-score-low { border-color: var(--color-text-tertiary); color: var(--color-text-tertiary); }

.rec-shortlist-btn {
  font-size: 12px;
  padding: 6px 14px;
  flex-shrink: 0;
  white-space: nowrap;
}
.btn-shortlisted {
  background: var(--color-success-bg);
  color: var(--color-success);
  border: 1px solid var(--color-success);
}

@media (max-width: 768px) {
  .rec-filter-grid { grid-template-columns: 1fr 1fr; }
  .rec-card { flex-wrap: wrap; }
  .rec-scores { order: -1; }
}
</style>
