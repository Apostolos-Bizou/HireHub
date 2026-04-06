<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useSearchStore } from "@/stores/search"

const router = useRouter()
const search = useSearchStore()
const ranks = ["3rd Officer","2nd Officer","Chief Officer","Master","3rd Engineer","2nd Engineer","Chief Engineer","Bosun","AB","Oiler","Cook"]
const vesselTypes = ["OIL_TANKER","CHEMICAL_TANKER","BULK_CARRIER","CONTAINER","LPG_CARRIER","LNG_CARRIER","GENERAL_CARGO","OFFSHORE","RO_RO","PASSENGER"]
const nationalities = ["Filipino","Ukrainian","Romanian","Bulgarian","Indian","Greek","Russian","Indonesian","Chinese","Polish"]
const hasFlag = ref(false)

function scoreClass(s) { return s >= 85 ? "ai-score-high" : s >= 70 ? "ai-score-mid" : "ai-score-low" }
function crewScoreClass(s) { return s >= 80 ? "crew-score-high" : s >= 60 ? "crew-score-mid" : "crew-score-low" }

/* Demo candidates shown before first search */
const demoCandidates = [
  { profileId: 101, fullName: "Juan Dela Cruz", currentRank: "3rd Officer", preferredVesselType: "OIL_TANKER", yearsOfExperience: 4, nationality: "Filipino", verifiedStatus: "VERIFIED", aiScore: 92, crewScore: 87, reviewCount: 14, topCertificates: ["COC","STCW","Tanker Fam."], availableFrom: "May 2026", flagEndorsements: ["Panama","Marshall Islands"] },
  { profileId: 102, fullName: "Ruslan Goncharov", currentRank: "2nd Officer", preferredVesselType: "BULK_CARRIER", yearsOfExperience: 6, nationality: "Ukrainian", verifiedStatus: "VERIFIED", aiScore: 87, crewScore: 82, reviewCount: 11, topCertificates: ["COC","STCW","ECDIS"], availableFrom: "Jun 2026", flagEndorsements: ["Liberia","Panama"] },
  { profileId: 103, fullName: "Andrei Marinescu", currentRank: "Chief Officer", preferredVesselType: "CONTAINER", yearsOfExperience: 8, nationality: "Romanian", verifiedStatus: "VERIFIED", aiScore: 81, crewScore: 88, reviewCount: 16, topCertificates: ["COC","STCW","BRM"], availableFrom: "Apr 2026", flagEndorsements: ["Malta"] },
  { profileId: 104, fullName: "Paolo Castillo", currentRank: "3rd Engineer", preferredVesselType: "LPG_CARRIER", yearsOfExperience: 3, nationality: "Filipino", verifiedStatus: "PENDING", aiScore: 76, crewScore: 71, reviewCount: 5, topCertificates: ["COC","STCW"], availableFrom: "Jul 2026", flagEndorsements: [] },
  { profileId: 105, fullName: "Lazar Stoyanov", currentRank: "AB", preferredVesselType: "GENERAL_CARGO", yearsOfExperience: 5, nationality: "Bulgarian", verifiedStatus: "PENDING", aiScore: 68, crewScore: 64, reviewCount: 3, topCertificates: ["STCW","PST"], availableFrom: "May 2026", flagEndorsements: ["Panama"] },
  { profileId: 106, fullName: "Dmitry Petrov", currentRank: "Chief Engineer", preferredVesselType: "OIL_TANKER", yearsOfExperience: 12, nationality: "Russian", verifiedStatus: "VERIFIED", aiScore: 95, crewScore: 91, reviewCount: 22, topCertificates: ["COC","STCW","ERM","IGS"], availableFrom: "Apr 2026", flagEndorsements: ["Liberia","Panama","Marshall Islands"] },
  { profileId: 107, fullName: "Miguel Santos", currentRank: "2nd Engineer", preferredVesselType: "CHEMICAL_TANKER", yearsOfExperience: 7, nationality: "Filipino", verifiedStatus: "VERIFIED", aiScore: 89, crewScore: 84, reviewCount: 9, topCertificates: ["COC","STCW","Chemical Tanker"], availableFrom: "May 2026", flagEndorsements: ["Panama","Bahamas"] }
]

const candidates = computed(() => search.results?.candidates || demoCandidates)
const totalResults = computed(() => search.results?.totalResults || demoCandidates.length)
const shortlisted = ref(new Set())

// Shortlist picker
const showShortlistPicker = ref(false)
const addingToShortlist = ref(false)
const addSuccess = ref(false)
const myShortlists = ref([
  { id: "1", title: "3rd Officer — Oil/Chemical Tanker", candidateCount: 5 },
  { id: "2", title: "Chief Engineer — Bulk Carrier", candidateCount: 3 },
  { id: "3", title: "2nd Officer — LPG Carrier", candidateCount: 8 }
])
const selectedShortlistId = ref('')

function toggleShortlist(id) {
  if (shortlisted.value.has(id)) shortlisted.value.delete(id)
  else shortlisted.value.add(id)
}

function openShortlistPicker() {
  if (shortlisted.value.size === 0) return
  selectedShortlistId.value = myShortlists.value[0]?.id || ''
  addSuccess.value = false
  showShortlistPicker.value = true
}

function addToShortlist() {
  if (!selectedShortlistId.value) return
  addingToShortlist.value = true
  const sl = myShortlists.value.find(s => s.id === selectedShortlistId.value)
  if (sl) sl.candidateCount += shortlisted.value.size
  setTimeout(() => {
    addingToShortlist.value = false
    addSuccess.value = true
    setTimeout(() => {
      showShortlistPicker.value = false
      addSuccess.value = false
      shortlisted.value.clear()
    }, 1200)
  }, 600)
}

function viewProfile(id) {
  router.push(`/seafarer/${id}`)
}
</script>

<template>
  <div class="container search-layout">
    <!-- Filter Panel -->
    <aside class="filter-panel card">
      <h3 class="filter-title">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"/></svg>
        Search Filters
      </h3>

      <div class="filter-group">
        <label>Rank</label>
        <select class="select" v-model="search.filters.rank">
          <option value="">Any rank</option>
          <option v-for="r in ranks" :key="r" :value="r">{{ r }}</option>
        </select>
      </div>

      <div class="filter-group">
        <label>Vessel Type</label>
        <select class="select" v-model="search.filters.vesselType">
          <option value="">Any vessel type</option>
          <option v-for="v in vesselTypes" :key="v" :value="v">{{ v.replace(/_/g,' ') }}</option>
        </select>
      </div>

      <div class="filter-group">
        <label>Min. Sea Service</label>
        <select class="select" v-model="search.filters.minExperience">
          <option :value="null">Any</option>
          <option :value="1">1+ years</option>
          <option :value="2">2+ years</option>
          <option :value="4">4+ years</option>
          <option :value="6">6+ years</option>
          <option :value="10">10+ years</option>
        </select>
      </div>

      <div class="filter-group">
        <label>Nationality</label>
        <select class="select" v-model="search.filters.nationality">
          <option value="">Any nationality</option>
          <option v-for="n in nationalities" :key="n" :value="n">{{ n }}</option>
        </select>
      </div>

      <div class="filter-group">
        <label>Available Before</label>
        <input class="input" type="date" v-model="search.filters.availableBefore" />
      </div>

      <div class="filter-divider"></div>

      <label class="checkbox-label">
        <input type="checkbox" v-model="search.filters.verifiedOnly" />
        <span class="checkbox-text">Verified only</span>
      </label>
      <label class="checkbox-label">
        <input type="checkbox" v-model="hasFlag" />
        <span class="checkbox-text">With flag endorsement</span>
      </label>
      <div class="filter-group" style="margin-top:var(--space-3)">
        <label>Min CrewScore</label>
        <select class="select" v-model="search.filters.minCrewScore">
          <option :value="0">Any</option>
          <option :value="60">60+</option>
          <option :value="70">70+</option>
          <option :value="80">80+</option>
          <option :value="90">90+</option>
        </select>
      </div>

      <div class="filter-divider"></div>

      <button class="btn btn-primary filter-btn" @click="search.searchCandidates">
        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
        Search
      </button>
      <button class="btn btn-tertiary filter-btn" @click="search.resetFilters">Reset filters</button>
    </aside>

    <!-- Results Panel -->
    <main class="results-panel">
      <!-- AI Banner -->
      <div class="ai-banner">
        <div class="ai-banner-icon">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2a4 4 0 0 1 4 4c0 1.95-1.4 3.58-3.25 3.93L12 22"/><path d="M12 2a4 4 0 0 0-4 4c0 1.95 1.4 3.58 3.25 3.93"/><path d="M5 10c0 1.5.8 2.8 2 3.5"/><path d="M19 10c0 1.5-.8 2.8-2 3.5"/><circle cx="12" cy="6" r="1.5"/></svg>
        </div>
        <div>
          <strong>AI matching is active</strong>
          <span class="ai-banner-sub">Candidates ranked by compatibility with your fleet requirements.</span>
        </div>
      </div>

      <!-- Results Header -->
      <div class="results-header">
        <span class="results-count">{{ totalResults }} candidates found</span>
        <select class="select sort-select" v-model="search.filters.sortBy">
          <option value="aiScore">AI match score</option>
          <option value="experience">Sea service</option>
          <option value="availability">Availability</option>
          <option value="recentlyActive">Recently active</option>
        </select>
      </div>

      <!-- Loading State -->
      <div v-if="search.loading" class="loading-state">
        <div class="loading-spinner"></div>
        <span>Searching candidates...</span>
      </div>

      <!-- Candidate Cards -->
      <div v-else class="candidate-list">
        <div
          v-for="c in candidates"
          :key="c.profileId"
          class="candidate-card card"
          :class="{ 'card-shortlisted': shortlisted.has(c.profileId) }"
        >
          <div class="candidate-main" @click="viewProfile(c.profileId)">
            <!-- Avatar -->
            <div class="avatar avatar-md avatar-seafarer">
              {{ c.fullName?.split(' ').map(w => w[0]).join('').substring(0, 2) }}
            </div>

            <!-- Info -->
            <div class="candidate-info">
              <div class="candidate-name-row">
                <span class="candidate-name">{{ c.fullName }}</span>
                <span v-if="c.verifiedStatus === 'VERIFIED'" class="badge badge-verified">
                  <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 24 24" fill="currentColor"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/></svg>
                  Verified
                </span>
              </div>
              <p class="candidate-headline">{{ c.currentRank }} · {{ c.preferredVesselType?.replace(/_/g, ' ') }} · {{ c.yearsOfExperience }} yrs experience</p>
              <div class="candidate-tags">
                <span class="tag tag-nationality">{{ c.nationality }}</span>
                <span v-for="cert in (c.topCertificates || []).slice(0, 4)" :key="cert" class="tag tag-cert">{{ cert }}</span>
                <span v-if="c.availableFrom" class="tag tag-avail">Available {{ c.availableFrom }}</span>
              </div>
              <div v-if="c.flagEndorsements?.length" class="candidate-flags">
                <svg xmlns="http://www.w3.org/2000/svg" width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z"/><line x1="4" y1="22" x2="4" y2="15"/></svg>
                <span v-for="f in c.flagEndorsements" :key="f" class="flag-tag">{{ f }}</span>
              </div>
            </div>
          </div>

          <!-- Actions Column -->
          <div class="candidate-actions">
            <div class="dual-scores">
              <div class="score-col">
                <div class="ai-score" :class="scoreClass(c.aiScore || 0)">{{ c.aiScore || '—' }}</div>
                <span class="score-type-label">AI</span>
              </div>
              <div class="score-col">
                <div class="ai-score crew-ring" :class="crewScoreClass(c.crewScore || 0)">{{ c.crewScore || '—' }}</div>
                <span class="score-type-label">Crew</span>
              </div>
            </div>
            <span v-if="c.reviewCount" class="review-count-tag">{{ c.reviewCount }} reviews</span>
            <button
              class="btn shortlist-btn"
              :class="shortlisted.has(c.profileId) ? 'btn-shortlisted' : 'btn-primary'"
              @click.stop="toggleShortlist(c.profileId)"
            >
              {{ shortlisted.has(c.profileId) ? '✓ Shortlisted' : '+ Shortlist' }}
            </button>
          </div>
        </div>
      </div>

      <!-- Shortlist Action Bar -->
      <Transition name="slide-up">
        <div v-if="shortlisted.size > 0" class="shortlist-bar">
          <div class="shortlist-bar-inner">
            <span class="shortlist-bar-count">{{ shortlisted.size }} candidate{{ shortlisted.size > 1 ? 's' : '' }} selected</span>
            <div class="shortlist-bar-actions">
              <button class="btn btn-tertiary btn-sm" @click="shortlisted.clear()">Clear</button>
              <button class="btn btn-primary btn-sm" @click="openShortlistPicker">
                <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"/><rect x="8" y="2" width="8" height="4" rx="1" ry="1"/></svg>
                Add to Shortlist
              </button>
            </div>
          </div>
        </div>
      </Transition>
    </main>

    <!-- Shortlist Picker Modal -->
    <div v-if="showShortlistPicker" class="modal-overlay" @click.self="showShortlistPicker = false">
      <div class="modal-card">
        <div v-if="addSuccess" class="modal-success">
          <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="#1B5E20" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
          <p class="modal-success-text">Added to shortlist!</p>
        </div>
        <template v-else>
          <div class="modal-header">
            <h2>Add to shortlist</h2>
            <button class="modal-close" @click="showShortlistPicker = false">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
            </button>
          </div>
          <div class="modal-body">
            <p class="modal-desc">Select which shortlist to add {{ shortlisted.size }} candidate{{ shortlisted.size > 1 ? 's' : '' }} to:</p>
            <div class="sl-picker-options">
              <label v-for="sl in myShortlists" :key="sl.id" class="sl-picker-option" :class="{ 'sl-picker-selected': selectedShortlistId === sl.id }">
                <input type="radio" :value="sl.id" v-model="selectedShortlistId" />
                <div class="sl-picker-info">
                  <strong>{{ sl.title }}</strong>
                  <span>{{ sl.candidateCount }} candidates</span>
                </div>
              </label>
            </div>
            <div class="sl-picker-or">
              <span>or</span>
            </div>
            <button class="btn btn-secondary" style="width:100%;text-align:center;" @click="showShortlistPicker = false; router.push('/shortlists')">
              + Create new shortlist first
            </button>
            <div class="modal-actions">
              <button class="btn btn-secondary" @click="showShortlistPicker = false">Cancel</button>
              <button class="btn btn-primary" @click="addToShortlist" :disabled="addingToShortlist || !selectedShortlistId">
                {{ addingToShortlist ? 'Adding...' : 'Add candidates' }}
              </button>
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<style scoped>
.search-layout {
  display: grid;
  grid-template-columns: 272px 1fr;
  gap: var(--space-5);
  padding-top: var(--space-5);
  padding-bottom: 80px;
}

/* --- Filter Panel --- */
.filter-panel {
  padding: var(--space-4);
  position: sticky;
  top: 76px;
  align-self: start;
  max-height: calc(100vh - 90px);
  overflow-y: auto;
}
.filter-title {
  font: var(--font-h3);
  margin-bottom: var(--space-4);
  display: flex;
  align-items: center;
  gap: var(--space-2);
  color: var(--color-text-primary);
}
.filter-group {
  margin-bottom: var(--space-3);
}
.filter-group label {
  display: block;
  font: var(--font-caption);
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-1);
}
.filter-divider {
  height: 1px;
  background: var(--color-border);
  margin: var(--space-3) 0;
}
.checkbox-label {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  margin-bottom: var(--space-2);
  cursor: pointer;
}
.checkbox-label input[type="checkbox"] {
  width: 16px;
  height: 16px;
  accent-color: var(--color-primary);
  cursor: pointer;
}
.checkbox-text {
  font: var(--font-small);
  color: var(--color-text-primary);
}
.filter-btn {
  width: 100%;
  margin-top: var(--space-2);
}

/* --- AI Banner --- */
.ai-banner {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3) var(--space-4);
  background: var(--color-primary-light);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-3);
}
.ai-banner-icon {
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
.ai-banner strong {
  font: var(--font-small);
  font-weight: 600;
  color: var(--color-primary-dark);
  display: block;
}
.ai-banner-sub {
  font: var(--font-caption);
  color: var(--color-text-secondary);
}

/* --- Results Header --- */
.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-3);
}
.results-count {
  font: var(--font-body);
  font-weight: 500;
}
.sort-select {
  width: auto;
  font: var(--font-small);
  padding: var(--space-1) var(--space-3);
}

/* --- Loading --- */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-8);
  color: var(--color-text-secondary);
  font: var(--font-body);
}
.loading-spinner {
  width: 32px;
  height: 32px;
  border: 3px solid var(--color-border);
  border-top-color: var(--color-primary);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

/* --- Candidate Cards --- */
.candidate-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}
.candidate-card {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  padding: var(--space-4);
  transition: box-shadow 0.15s, border-color 0.15s;
  cursor: default;
}
.candidate-card:hover {
  box-shadow: var(--shadow-md);
}
.card-shortlisted {
  border-color: var(--color-primary);
  background: rgba(10, 102, 194, 0.02);
}
.candidate-main {
  display: flex;
  align-items: flex-start;
  gap: var(--space-3);
  flex: 1;
  cursor: pointer;
  min-width: 0;
}
.candidate-info {
  flex: 1;
  min-width: 0;
}
.candidate-name-row {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  flex-wrap: wrap;
}
.candidate-name {
  font: var(--font-body);
  font-weight: 600;
  color: var(--color-text-primary);
}
.candidate-headline {
  font: var(--font-small);
  color: var(--color-text-secondary);
  margin-top: 2px;
}
.candidate-tags {
  display: flex;
  gap: 6px;
  margin-top: var(--space-2);
  flex-wrap: wrap;
}
.tag {
  padding: 2px 8px;
  border-radius: 4px;
  font: var(--font-caption);
  font-weight: 500;
}
.tag-nationality {
  background: var(--color-primary-light);
  color: var(--color-primary);
}
.tag-cert {
  background: var(--color-surface);
  color: var(--color-text-secondary);
}
.tag-avail {
  background: var(--color-success-bg);
  color: var(--color-success);
}
.candidate-flags {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-top: 6px;
  color: var(--color-text-tertiary);
  font: var(--font-caption);
}
.flag-tag {
  padding: 1px 6px;
  background: var(--color-surface);
  border-radius: 3px;
  font: var(--font-caption);
  color: var(--color-text-secondary);
}

/* --- Actions Column --- */
.candidate-actions {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-2);
  flex-shrink: 0;
}
.dual-scores {
  display: flex;
  gap: var(--space-2);
}
.score-col {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}
.score-type-label {
  font: var(--font-caption);
  color: var(--color-text-tertiary);
  text-transform: uppercase;
  font-size: 9px;
  letter-spacing: 0.5px;
}
.crew-ring { /* CrewScore uses green tones */ }
.crew-score-high { border-color: var(--color-success) !important; color: var(--color-success) !important; }
.crew-score-mid { border-color: var(--color-agent) !important; color: var(--color-agent) !important; }
.crew-score-low { border-color: var(--color-text-tertiary) !important; color: var(--color-text-tertiary) !important; }
.review-count-tag {
  font: var(--font-caption);
  color: var(--color-text-tertiary);
  background: var(--color-surface);
  padding: 2px 6px;
  border-radius: 4px;
}
.shortlist-btn {
  font-size: 12px;
  padding: 6px 14px;
  white-space: nowrap;
}
.btn-shortlisted {
  background: var(--color-success-bg);
  color: var(--color-success);
  border: 1px solid var(--color-success);
}
.btn-shortlisted:hover {
  background: #C8E6C9;
}

/* --- Shortlist Action Bar --- */
.shortlist-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: var(--color-white);
  border-top: 1px solid var(--color-border);
  box-shadow: 0 -4px 12px rgba(0,0,0,0.08);
  z-index: 50;
  padding: var(--space-3) var(--space-4);
}
.shortlist-bar-inner {
  max-width: 1128px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.shortlist-bar-count {
  font: var(--font-body);
  font-weight: 600;
  color: var(--color-primary);
}
.shortlist-bar-actions {
  display: flex;
  gap: var(--space-2);
}
.btn-sm {
  font-size: 12px;
  padding: 6px 14px;
}

/* Transition */
.slide-up-enter-active, .slide-up-leave-active { transition: transform 0.25s ease, opacity 0.25s ease; }
.slide-up-enter-from, .slide-up-leave-to { transform: translateY(100%); opacity: 0; }

/* --- Responsive --- */
@media (max-width: 768px) {
  .search-layout { grid-template-columns: 1fr; }
  .filter-panel { position: static; max-height: none; }
  .candidate-card { flex-direction: column; align-items: stretch; }
  .candidate-main { flex-direction: column; }
  .candidate-actions { flex-direction: row; justify-content: space-between; margin-top: var(--space-3); padding-top: var(--space-3); border-top: 1px solid var(--color-border); }
  .ai-score-wrapper { flex-direction: row; gap: var(--space-2); }
}

/* Modal */
.modal-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 300;
  padding: var(--space-4);
}
.modal-card {
  background: var(--color-white);
  border-radius: var(--radius-lg);
  width: 100%;
  max-width: 480px;
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
  background: none; border: none; cursor: pointer;
  color: var(--color-text-tertiary);
  padding: var(--space-1); border-radius: var(--radius-sm);
}
.modal-close:hover { background: var(--color-surface); }
.modal-body { padding: var(--space-5); }
.modal-desc { font: var(--font-small); color: var(--color-text-secondary); margin-bottom: var(--space-4); }
.modal-actions {
  display: flex; justify-content: flex-end; gap: var(--space-2);
  padding-top: var(--space-4); margin-top: var(--space-4);
  border-top: 1px solid var(--color-border);
}
.modal-success {
  display: flex; flex-direction: column; align-items: center; gap: var(--space-3);
  padding: var(--space-8) var(--space-5);
}
.modal-success-text { font: var(--font-h3); color: var(--color-success); }

.sl-picker-options { display: flex; flex-direction: column; gap: var(--space-2); margin-bottom: var(--space-3); }
.sl-picker-option {
  display: flex; align-items: center; gap: var(--space-3);
  padding: var(--space-3) var(--space-4);
  border: 1.5px solid var(--color-border); border-radius: var(--radius-md);
  cursor: pointer; transition: border-color 0.15s, background 0.15s;
}
.sl-picker-option:hover { border-color: var(--color-primary); background: var(--color-primary-light); }
.sl-picker-selected { border-color: var(--color-primary); background: var(--color-primary-light); }
.sl-picker-option input[type="radio"] { display: none; }
.sl-picker-info strong { display: block; font: var(--font-body); font-weight: 500; }
.sl-picker-info span { font: var(--font-caption); color: var(--color-text-secondary); }
.sl-picker-or {
  text-align: center; margin: var(--space-3) 0;
  font: var(--font-caption); color: var(--color-text-tertiary);
}
</style>
