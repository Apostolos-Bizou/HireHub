<script setup>
import { useSearchStore } from "@/stores/search"

const search = useSearchStore()
const ranks = ["3rd Officer","2nd Officer","Chief Officer","Master","3rd Engineer","2nd Engineer","Chief Engineer","Bosun","AB","Oiler","Cook"]
const vesselTypes = ["OIL_TANKER","CHEMICAL_TANKER","BULK_CARRIER","CONTAINER","LPG_CARRIER","LNG_CARRIER","GENERAL_CARGO","OFFSHORE","RO_RO","PASSENGER"]
const nationalities = ["Filipino","Ukrainian","Romanian","Bulgarian","Indian","Greek","Russian","Indonesian","Chinese","Polish"]

function scoreClass(s) { return s >= 85 ? "ai-score-high" : s >= 70 ? "ai-score-mid" : "ai-score-low" }
</script>

<template>
  <div class="container search-layout">
    <aside class="filter-panel card">
      <h3>Search Filters</h3>
      <div class="filter-group">
        <label>Rank</label>
        <select class="select" v-model="search.filters.rank"><option value="">Any</option><option v-for="r in ranks" :key="r" :value="r">{{ r }}</option></select>
      </div>
      <div class="filter-group">
        <label>Vessel Type</label>
        <select class="select" v-model="search.filters.vesselType"><option value="">Any</option><option v-for="v in vesselTypes" :key="v" :value="v">{{ v.replace(/_/g,' ') }}</option></select>
      </div>
      <div class="filter-group">
        <label>Nationality</label>
        <select class="select" v-model="search.filters.nationality"><option value="">Any</option><option v-for="n in nationalities" :key="n" :value="n">{{ n }}</option></select>
      </div>
      <div class="filter-group">
        <label>Min. Sea Service (years)</label>
        <select class="select" v-model="search.filters.minExperience"><option :value="null">Any</option><option :value="1">1+</option><option :value="2">2+</option><option :value="4">4+</option><option :value="6">6+</option><option :value="10">10+</option></select>
      </div>
      <div class="filter-group">
        <label>Available Before</label>
        <input class="input" type="date" v-model="search.filters.availableBefore" />
      </div>
      <label class="checkbox-label"><input type="checkbox" v-model="search.filters.verifiedOnly" /> Verified only</label>
      <button class="btn btn-primary" style="width:100%;margin-top:var(--space-4)" @click="search.searchCandidates">Search</button>
      <button class="btn btn-tertiary" style="width:100%;margin-top:var(--space-2)" @click="search.resetFilters">Reset</button>
    </aside>

    <main class="results-panel">
      <div class="ai-banner badge-info">🤖 AI matching is active. Candidates ranked by compatibility with your fleet requirements.</div>
      <div class="results-header">
        <span v-if="search.results">{{ search.results.totalResults }} candidates found</span>
        <span v-else>Enter criteria and search</span>
        <select class="select" style="width:auto" v-model="search.filters.sortBy">
          <option value="aiScore">AI match score</option>
          <option value="experience">Sea service</option>
          <option value="availability">Availability</option>
        </select>
      </div>

      <div v-if="search.loading" class="loading">Searching...</div>

      <div v-if="search.results" class="candidate-list">
        <div v-for="c in search.results.candidates" :key="c.profileId" class="card candidate-card">
          <div class="avatar avatar-md avatar-seafarer">{{ c.fullName?.split(' ').map(w=>w[0]).join('').substring(0,2) }}</div>
          <div class="candidate-info">
            <div class="candidate-name">
              {{ c.fullName }}
              <span v-if="c.verifiedStatus==='VERIFIED'" class="badge badge-verified">✓ Verified</span>
            </div>
            <p class="candidate-headline">{{ c.currentRank }} │ {{ c.preferredVesselType?.replace(/_/g,' ') }} │ {{ c.yearsOfExperience }} yrs</p>
            <div class="candidate-tags">
              <span class="badge badge-info">{{ c.nationality }}</span>
              <span v-for="cert in c.topCertificates" :key="cert" class="badge badge-info">{{ cert }}</span>
            </div>
          </div>
          <div class="candidate-actions">
            <div class="ai-score" :class="scoreClass(c.aiScore || 0)">{{ c.aiScore || '—' }}</div>
            <button class="btn btn-primary btn-sm">+ Shortlist</button>
          </div>
        </div>
      </div>

      <!-- Demo data -->
      <div v-if="!search.results && !search.loading" class="candidate-list">
        <div v-for="i in 5" :key="i" class="card candidate-card demo">
          <div class="avatar avatar-md avatar-seafarer">{{ ['JD','RG','AM','PC','LS'][i-1] }}</div>
          <div class="candidate-info">
            <div class="candidate-name">{{ ['Juan Dela Cruz','Ruslan Goncharov','Andrei Marinescu','Paolo Castillo','Lazar Stoyanov'][i-1] }} <span v-if="i<=3" class="badge badge-verified">✓ Verified</span></div>
            <p class="candidate-headline">{{ ['3rd Officer │ Oil/Chemical Tanker │ 4 yrs','2nd Officer │ Bulk Carrier │ 6 yrs','Chief Officer │ Container │ 8 yrs','3rd Engineer │ LPG Carrier │ 3 yrs','AB │ General Cargo │ 5 yrs'][i-1] }}</p>
            <div class="candidate-tags">
              <span class="badge badge-info">{{ ['Filipino','Ukrainian','Romanian','Filipino','Bulgarian'][i-1] }}</span>
              <span class="badge badge-info">COC</span><span class="badge badge-info">STCW</span>
            </div>
          </div>
          <div class="candidate-actions">
            <div class="ai-score" :class="scoreClass([92,87,81,76,68][i-1])">{{ [92,87,81,76,68][i-1] }}</div>
            <button class="btn btn-primary btn-sm">+ Shortlist</button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
.search-layout { display: grid; grid-template-columns: 260px 1fr; gap: var(--space-5); padding-top: var(--space-5); padding-bottom: var(--space-8); }
.filter-panel { padding: var(--space-4); position: sticky; top: 76px; align-self: start; }
.filter-panel h3 { font: var(--font-h2); margin-bottom: var(--space-4); }
.filter-group { margin-bottom: var(--space-3); }
.filter-group label { display: block; font: var(--font-small); font-weight: 500; margin-bottom: var(--space-1); }
.checkbox-label { display: flex; align-items: center; gap: var(--space-2); font: var(--font-small); margin-top: var(--space-3); cursor: pointer; }
.ai-banner { padding: var(--space-3) var(--space-4); border-radius: var(--radius-md); margin-bottom: var(--space-3); font: var(--font-small); }
.results-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--space-3); font: var(--font-small); font-weight: 500; }
.candidate-card { display: flex; align-items: center; gap: var(--space-3); padding: var(--space-4); margin-bottom: var(--space-2); }
.candidate-card:hover { box-shadow: var(--shadow-md); }
.candidate-info { flex: 1; }
.candidate-name { font: var(--font-body); font-weight: 500; display: flex; align-items: center; gap: var(--space-2); }
.candidate-headline { font: var(--font-small); color: var(--color-text-secondary); margin-top: 2px; }
.candidate-tags { display: flex; gap: var(--space-1); margin-top: var(--space-2); flex-wrap: wrap; }
.candidate-actions { display: flex; flex-direction: column; align-items: center; gap: var(--space-2); }
.btn-sm { font-size: 12px; padding: 4px 12px; }
.loading { text-align: center; padding: var(--space-8); font: var(--font-body); color: var(--color-text-secondary); }
@media (max-width: 768px) { .search-layout { grid-template-columns: 1fr; } .filter-panel { position: static; } }
</style>
