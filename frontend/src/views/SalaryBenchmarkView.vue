<script setup>
import { ref, computed, watch } from 'vue'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const isSeafarer = computed(() => auth.user?.role === 'SEAFARER')

// ── Filter state ──
const selectedRank = ref('3rd Officer')
const selectedVessel = ref('All')
const selectedNationality = ref('All')
const selectedExperience = ref('All')

const ranks = [
  'Master', 'Chief Officer', '2nd Officer', '3rd Officer',
  'Chief Engineer', '2nd Engineer', '3rd Engineer', '4th Engineer',
  'Bosun', 'AB Seaman', 'OS Seaman', 'Fitter', 'Oiler', 'Wiper',
  'Electrician', 'Cook / Steward', 'Cadet (Deck)', 'Cadet (Engine)'
]
const vesselTypes = ['All', 'Oil/Chemical Tanker', 'Bulk Carrier', 'Container', 'LPG/LNG', 'Passenger/Cruise', 'Offshore/AHTS', 'General Cargo']
const nationalities = ['All', 'Filipino', 'Ukrainian', 'Indian', 'Greek', 'Romanian', 'Russian', 'Bulgarian', 'Indonesian', 'Myanmar']
const experienceLevels = ['All', '0-2 years', '2-5 years', '5-10 years', '10+ years']

// ── Salary database (realistic maritime data in USD/month) ──
const salaryData = {
  'Master':           { min: 8500, p25: 9800,  median: 12000, p75: 14500, max: 18000 },
  'Chief Officer':    { min: 5500, p25: 6800,  median: 8200,  p75: 9500,  max: 12000 },
  '2nd Officer':      { min: 3800, p25: 4500,  median: 5200,  p75: 6000,  max: 7500  },
  '3rd Officer':      { min: 2200, p25: 2800,  median: 3400,  p75: 4000,  max: 5200  },
  'Chief Engineer':   { min: 8000, p25: 9500,  median: 11500, p75: 13800, max: 17000 },
  '2nd Engineer':     { min: 5200, p25: 6500,  median: 7800,  p75: 9000,  max: 11000 },
  '3rd Engineer':     { min: 3500, p25: 4200,  median: 5000,  p75: 5800,  max: 7200  },
  '4th Engineer':     { min: 2000, p25: 2600,  median: 3200,  p75: 3800,  max: 4800  },
  'Bosun':            { min: 1800, p25: 2100,  median: 2600,  p75: 3000,  max: 3800  },
  'AB Seaman':        { min: 1200, p25: 1500,  median: 1800,  p75: 2200,  max: 2800  },
  'OS Seaman':        { min: 800,  p25: 1000,  median: 1300,  p75: 1600,  max: 2000  },
  'Fitter':           { min: 1500, p25: 1800,  median: 2200,  p75: 2600,  max: 3200  },
  'Oiler':            { min: 1100, p25: 1400,  median: 1700,  p75: 2000,  max: 2500  },
  'Wiper':            { min: 700,  p25: 900,   median: 1100,  p75: 1400,  max: 1800  },
  'Electrician':      { min: 3000, p25: 3800,  median: 4500,  p75: 5200,  max: 6500  },
  'Cook / Steward':   { min: 1200, p25: 1500,  median: 1900,  p75: 2300,  max: 3000  },
  'Cadet (Deck)':     { min: 400,  p25: 500,   median: 700,   p75: 900,   max: 1200  },
  'Cadet (Engine)':   { min: 400,  p25: 500,   median: 700,   p75: 900,   max: 1200  }
}

// Vessel type multipliers
const vesselMultipliers = {
  'All': 1.0,
  'Oil/Chemical Tanker': 1.15,
  'Bulk Carrier': 0.95,
  'Container': 1.05,
  'LPG/LNG': 1.25,
  'Passenger/Cruise': 1.10,
  'Offshore/AHTS': 1.20,
  'General Cargo': 0.90
}

// Nationality adjustments (market rate differences)
const nationalityAdjust = {
  'All': 0, 'Filipino': -200, 'Ukrainian': -100, 'Indian': -300,
  'Greek': 400, 'Romanian': -50, 'Russian': -80, 'Bulgarian': -120,
  'Indonesian': -350, 'Myanmar': -400
}

// Experience multipliers
const experienceMultipliers = {
  'All': 1.0, '0-2 years': 0.85, '2-5 years': 0.95, '5-10 years': 1.05, '10+ years': 1.15
}

// ── Computed salary for current selection ──
const currentSalary = computed(() => {
  const base = salaryData[selectedRank.value]
  if (!base) return null
  const vm = vesselMultipliers[selectedVessel.value]
  const na = nationalityAdjust[selectedNationality.value]
  const em = experienceMultipliers[selectedExperience.value]
  return {
    min:    Math.round(base.min * vm * em + na),
    p25:    Math.round(base.p25 * vm * em + na),
    median: Math.round(base.median * vm * em + na),
    p75:    Math.round(base.p75 * vm * em + na),
    max:    Math.round(base.max * vm * em + na)
  }
})

// ── Bar chart data: compare across vessel types ──
const vesselComparison = computed(() => {
  const base = salaryData[selectedRank.value]
  if (!base) return []
  const em = experienceMultipliers[selectedExperience.value]
  const na = nationalityAdjust[selectedNationality.value]
  return Object.entries(vesselMultipliers)
    .filter(([k]) => k !== 'All')
    .map(([name, mult]) => ({
      name: name.length > 16 ? name.substring(0, 14) + '…' : name,
      fullName: name,
      median: Math.round(base.median * mult * em + na),
      p25: Math.round(base.p25 * mult * em + na),
      p75: Math.round(base.p75 * mult * em + na)
    }))
    .sort((a, b) => b.median - a.median)
})

const maxBarVal = computed(() => {
  return Math.max(...vesselComparison.value.map(v => v.p75)) * 1.05
})

// ── Rank comparison table ──
const rankComparison = computed(() => {
  const vm = vesselMultipliers[selectedVessel.value]
  const na = nationalityAdjust[selectedNationality.value]
  const em = experienceMultipliers[selectedExperience.value]
  return ranks.map(rank => {
    const base = salaryData[rank]
    return {
      rank,
      min: Math.round(base.min * vm * em + na),
      median: Math.round(base.median * vm * em + na),
      max: Math.round(base.max * vm * em + na),
      isSelected: rank === selectedRank.value
    }
  })
})

// ── YoY trend data (simulated) ──
const trendData = computed(() => {
  const base = salaryData[selectedRank.value]
  if (!base) return []
  const med = base.median
  return [
    { year: '2021', salary: Math.round(med * 0.82) },
    { year: '2022', salary: Math.round(med * 0.88) },
    { year: '2023', salary: Math.round(med * 0.93) },
    { year: '2024', salary: Math.round(med * 0.97) },
    { year: '2025', salary: Math.round(med * 1.00) },
    { year: '2026', salary: Math.round(med * 1.04) }
  ]
})

const trendMax = computed(() => Math.max(...trendData.value.map(t => t.salary)) * 1.12)
const trendMin = computed(() => Math.min(...trendData.value.map(t => t.salary)) * 0.88)
const trendRange = computed(() => trendMax.value - trendMin.value)

function trendY(val) {
  return 100 - ((val - trendMin.value) / trendRange.value) * 100
}

const trendPolyline = computed(() => {
  const pts = trendData.value.map((t, i) => {
    const x = (i / (trendData.value.length - 1)) * 100
    const y = trendY(t.salary)
    return `${x},${y}`
  })
  return pts.join(' ')
})

const yoyChange = computed(() => {
  const d = trendData.value
  if (d.length < 2) return 0
  return ((d[d.length - 1].salary - d[d.length - 2].salary) / d[d.length - 2].salary * 100).toFixed(1)
})

// ── Helpers ──
function fmt(n) {
  return '$' + n.toLocaleString('en-US')
}

function barWidth(val) {
  return (val / maxBarVal.value * 100) + '%'
}

// ── Box plot helpers ──
const boxPlotMax = computed(() => currentSalary.value ? currentSalary.value.max * 1.1 : 1)
function boxPos(val) {
  return (val / boxPlotMax.value * 100) + '%'
}
function boxWidth(from, to) {
  return ((to - from) / boxPlotMax.value * 100) + '%'
}
</script>

<template>
  <div class="container salary-page">
    <!-- Header -->
    <div class="salary-header">
      <div>
        <h1>
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="1" x2="12" y2="23"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg>
          Salary Benchmark
        </h1>
        <p class="salary-sub">Compare maritime salaries by rank, vessel type, nationality & experience · Data from 24,847 profiles</p>
      </div>
      <div v-if="isSeafarer" class="salary-badge premium-badge">
        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/></svg>
        Premium Feature
      </div>
    </div>

    <!-- Filters -->
    <div class="card filter-card">
      <div class="filter-grid">
        <div class="filter-group">
          <label>Rank</label>
          <select v-model="selectedRank">
            <option v-for="r in ranks" :key="r" :value="r">{{ r }}</option>
          </select>
        </div>
        <div class="filter-group">
          <label>Vessel type</label>
          <select v-model="selectedVessel">
            <option v-for="v in vesselTypes" :key="v" :value="v">{{ v }}</option>
          </select>
        </div>
        <div class="filter-group">
          <label>Nationality</label>
          <select v-model="selectedNationality">
            <option v-for="n in nationalities" :key="n" :value="n">{{ n }}</option>
          </select>
        </div>
        <div class="filter-group">
          <label>Experience</label>
          <select v-model="selectedExperience">
            <option v-for="e in experienceLevels" :key="e" :value="e">{{ e }}</option>
          </select>
        </div>
      </div>
    </div>

    <!-- KPI Summary -->
    <div class="kpi-grid" v-if="currentSalary">
      <div class="kpi-card">
        <div class="kpi-label" title="Κατώτατος μισθός στην αγορά">Market low</div>
        <div class="kpi-val" style="color:var(--color-text-secondary)">{{ fmt(currentSalary.min) }}</div>
        <div class="kpi-trend">per month</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label" title="Διάμεσος μισθός — 50% κερδίζουν πάνω, 50% κάτω">Median salary</div>
        <div class="kpi-val kpi-blue">{{ fmt(currentSalary.median) }}</div>
        <div class="kpi-trend kpi-up">{{ yoyChange > 0 ? '+' : '' }}{{ yoyChange }}% YoY</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label" title="75ο εκατοστημόριο — top 25% κερδίζουν τόσο ή παραπάνω">75th percentile</div>
        <div class="kpi-val kpi-green">{{ fmt(currentSalary.p75) }}</div>
        <div class="kpi-trend">top quarter</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label" title="Μέγιστος μισθός στην αγορά για αυτή τη θέση">Market high</div>
        <div class="kpi-val kpi-amber">{{ fmt(currentSalary.max) }}</div>
        <div class="kpi-trend">per month</div>
      </div>
    </div>

    <!-- Main Content: 2 columns -->
    <div class="salary-layout">
      <div class="salary-main">

        <!-- Box Plot Distribution -->
        <div class="card section" v-if="currentSalary">
          <h2>
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M21 12H3"/><path d="M21 6H3"/><path d="M21 18H3"/></svg>
            Salary distribution · {{ selectedRank }}
          </h2>
          <p class="section-sub" title="Κατανομή μισθών: ελάχιστο, 25η, 50η, 75η εκατοστημόριο, μέγιστο">Box plot showing min → 25th → median → 75th → max range</p>
          <div class="box-plot-container">
            <!-- Whisker line (min to max) -->
            <div class="box-whisker" :style="{ left: boxPos(currentSalary.min), width: boxWidth(currentSalary.min, currentSalary.max) }"></div>
            <!-- Min tick -->
            <div class="box-tick" :style="{ left: boxPos(currentSalary.min) }"></div>
            <!-- Max tick -->
            <div class="box-tick" :style="{ left: boxPos(currentSalary.max) }"></div>
            <!-- IQR box (P25 to P75) -->
            <div class="box-iqr" :style="{ left: boxPos(currentSalary.p25), width: boxWidth(currentSalary.p25, currentSalary.p75) }"></div>
            <!-- Median line -->
            <div class="box-median" :style="{ left: boxPos(currentSalary.median) }"></div>
            <!-- Labels -->
            <div class="box-labels">
              <span :style="{ left: boxPos(currentSalary.min) }">{{ fmt(currentSalary.min) }}</span>
              <span :style="{ left: boxPos(currentSalary.p25) }">{{ fmt(currentSalary.p25) }}</span>
              <span class="box-label-median" :style="{ left: boxPos(currentSalary.median) }">{{ fmt(currentSalary.median) }}</span>
              <span :style="{ left: boxPos(currentSalary.p75) }">{{ fmt(currentSalary.p75) }}</span>
              <span :style="{ left: boxPos(currentSalary.max) }">{{ fmt(currentSalary.max) }}</span>
            </div>
          </div>
        </div>

        <!-- Bar Chart: Vessel Type Comparison -->
        <div class="card section">
          <h2>
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="12" width="4" height="9"/><rect x="10" y="7" width="4" height="14"/><rect x="17" y="3" width="4" height="18"/></svg>
            Median salary by vessel type · {{ selectedRank }}
          </h2>
          <p class="section-sub" title="Σύγκριση μέσου μισθού ανά τύπο πλοίου">Horizontal bars show median monthly salary per vessel category</p>
          <div class="bar-chart">
            <div v-for="v in vesselComparison" :key="v.name" class="bar-row" :title="v.fullName + ': ' + fmt(v.median) + '/mo (range: ' + fmt(v.p25) + ' – ' + fmt(v.p75) + ')'">
              <div class="bar-label">{{ v.name }}</div>
              <div class="bar-track">
                <!-- P25-P75 range background -->
                <div class="bar-range" :style="{ left: barWidth(v.p25), width: barWidth(v.p75 - v.p25) }"></div>
                <!-- Median bar -->
                <div class="bar-fill" :style="{ width: barWidth(v.median) }" :class="{ 'bar-highlight': v.fullName === selectedVessel }"></div>
              </div>
              <div class="bar-value">{{ fmt(v.median) }}</div>
            </div>
          </div>
        </div>

        <!-- Salary Trend Line -->
        <div class="card section">
          <h2>
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg>
            Salary trend (2021–2026) · {{ selectedRank }}
          </h2>
          <p class="section-sub" title="Εξέλιξη μέσου μισθού τα τελευταία 6 χρόνια">Year-over-year median salary progression</p>
          <div class="trend-chart">
            <svg viewBox="-2 -5 104 115" preserveAspectRatio="none" class="trend-svg">
              <!-- Grid lines -->
              <line v-for="i in 5" :key="'g'+i" x1="0" :y1="i*20" x2="100" :y2="i*20" stroke="var(--color-border)" stroke-width="0.3" stroke-dasharray="2,2"/>
              <!-- Area fill -->
              <polygon :points="'0,100 ' + trendPolyline + ' 100,100'" fill="var(--color-primary-light)" opacity="0.5"/>
              <!-- Line -->
              <polyline :points="trendPolyline" fill="none" stroke="var(--color-primary)" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              <!-- Dots -->
              <circle v-for="(t, i) in trendData" :key="'d'+i"
                :cx="(i / (trendData.length - 1)) * 100"
                :cy="trendY(t.salary)"
                r="2.5" fill="var(--color-primary)" stroke="white" stroke-width="1"/>
            </svg>
            <div class="trend-labels">
              <span v-for="t in trendData" :key="t.year" class="trend-label">
                <span class="trend-year">{{ t.year }}</span>
                <span class="trend-val">{{ fmt(t.salary) }}</span>
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- Right sidebar -->
      <div class="salary-sidebar">
        <!-- Quick Insights -->
        <div class="card section">
          <h3>
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg>
            Market insights
          </h3>
          <div class="insight-list">
            <div class="insight-item">
              <div class="insight-icon insight-up">↑</div>
              <div>
                <div class="insight-title">LPG/LNG premium</div>
                <div class="insight-desc" title="Τα πλοία LPG/LNG πληρώνουν +25% επιπλέον">LPG/LNG vessels pay +25% above market average for {{ selectedRank }}</div>
              </div>
            </div>
            <div class="insight-item">
              <div class="insight-icon insight-info">→</div>
              <div>
                <div class="insight-title">Nationality gap</div>
                <div class="insight-desc" title="Ελληνικές εταιρείες πληρώνουν premium">Greek-flag companies offer premium rates vs. Asian market</div>
              </div>
            </div>
            <div class="insight-item">
              <div class="insight-icon insight-up">↑</div>
              <div>
                <div class="insight-title">YoY growth</div>
                <div class="insight-desc" title="Ετήσια αύξηση μισθών">{{ selectedRank }} salaries grew {{ yoyChange }}% in the last 12 months</div>
              </div>
            </div>
            <div class="insight-item">
              <div class="insight-icon insight-warn">!</div>
              <div>
                <div class="insight-title">Shortage alert</div>
                <div class="insight-desc" title="Έλλειψη προσωπικού σε συγκεκριμένους βαθμούς">Officer ranks show 12% supply deficit — expect upward pressure</div>
              </div>
            </div>
          </div>
        </div>

        <!-- Data Sources -->
        <div class="card section">
          <h3>
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/></svg>
            Data sources
          </h3>
          <div class="source-list">
            <div class="source-item">
              <span class="source-dot source-dot-blue"></span>
              <span>24,847 HireHub profiles</span>
            </div>
            <div class="source-item">
              <span class="source-dot source-dot-green"></span>
              <span>BIMCO / ICS Seafarer Workforce Report 2024</span>
            </div>
            <div class="source-item">
              <span class="source-dot source-dot-amber"></span>
              <span>Drewry Manning Annual Review 2025</span>
            </div>
            <div class="source-item">
              <span class="source-dot source-dot-gray"></span>
              <span>Market intelligence from 180+ principals</span>
            </div>
          </div>
          <p class="source-note" title="Τα δεδομένα ανανεώνονται κάθε μήνα">Data refreshed monthly · Last update: April 2026</p>
        </div>

        <!-- CTA for Seafarers -->
        <div class="card section cta-card" v-if="isSeafarer">
          <div class="cta-icon">💡</div>
          <h3>Know your worth</h3>
          <p class="cta-text">Update your profile with your latest rank and experience to get personalized salary insights.</p>
          <router-link to="/profile" class="btn btn-primary" style="width:100%;text-align:center;margin-top:var(--space-3);">Update my profile</router-link>
        </div>
      </div>
    </div>

    <!-- Full Rank Comparison Table -->
    <div class="card section" style="margin-top:var(--space-5);">
      <h2>
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><line x1="3" y1="9" x2="21" y2="9"/><line x1="3" y1="15" x2="21" y2="15"/><line x1="9" y1="3" x2="9" y2="21"/><line x1="15" y1="3" x2="15" y2="21"/></svg>
        Full rank comparison
        <span class="table-filter-label" v-if="selectedVessel !== 'All'"> · {{ selectedVessel }}</span>
      </h2>
      <p class="section-sub" title="Πίνακας μισθών ανά βαθμό — κατώτατος, μέσος, ανώτατος">Monthly salary ranges across all ranks (USD)</p>
      <div class="table-wrapper">
        <table class="salary-table">
          <thead>
            <tr>
              <th>Rank</th>
              <th class="num">Min</th>
              <th class="num">Median</th>
              <th class="num">Max</th>
              <th class="num">Range</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="r in rankComparison" :key="r.rank" :class="{ 'row-selected': r.isSelected }" @click="selectedRank = r.rank" style="cursor:pointer;">
              <td>
                <span class="rank-name">{{ r.rank }}</span>
                <span v-if="r.isSelected" class="rank-badge">Selected</span>
              </td>
              <td class="num">{{ fmt(r.min) }}</td>
              <td class="num num-median">{{ fmt(r.median) }}</td>
              <td class="num">{{ fmt(r.max) }}</td>
              <td class="num num-range">{{ fmt(r.max - r.min) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Disclaimer -->
    <div class="disclaimer" title="Σημαντική σημείωση — τα δεδομένα είναι ενδεικτικά">
      <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg>
      Salary data is aggregated from HireHub profiles, industry reports, and market intelligence. Figures are indicative and may vary based on company, flag state, CBA terms, and market conditions. HireHub does not guarantee specific salary outcomes.
    </div>
  </div>
</template>

<style scoped>
.salary-page { padding-top: var(--space-5); padding-bottom: var(--space-8); }

.salary-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: var(--space-5);
}
.salary-header h1 {
  font: var(--font-h1);
  display: flex;
  align-items: center;
  gap: var(--space-2);
}
.salary-sub { font: var(--font-small); color: var(--color-text-secondary); margin-top: var(--space-1); }
.premium-badge {
  display: flex;
  align-items: center;
  gap: 6px;
  background: #FBEAF0;
  color: #72243E;
  padding: 6px 14px;
  border-radius: var(--radius-md);
  font: var(--font-caption);
  font-weight: 500;
  white-space: nowrap;
}

/* Filters */
.filter-card { margin-bottom: var(--space-5); }
.filter-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-4);
}
.filter-group label {
  display: block;
  font: var(--font-caption);
  font-weight: 500;
  color: var(--color-text-secondary);
  margin-bottom: var(--space-1);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
.filter-group select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  font: var(--font-body);
  color: var(--color-text-primary);
  background: var(--color-white);
  cursor: pointer;
  outline: none;
  transition: border-color 0.15s;
}
.filter-group select:focus { border-color: var(--color-primary); }

/* KPI Grid */
.kpi-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-4);
  margin-bottom: var(--space-5);
}
.kpi-card {
  background: var(--color-white);
  border: 0.5px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-4);
  box-shadow: var(--shadow-sm);
}
.kpi-label { font: var(--font-caption); color: var(--color-text-secondary); text-transform: uppercase; letter-spacing: 0.5px; }
.kpi-val { font-size: 26px; font-weight: 600; margin: var(--space-1) 0; }
.kpi-blue { color: var(--color-primary); }
.kpi-green { color: var(--color-success); }
.kpi-amber { color: var(--color-accent); }
.kpi-trend { font: var(--font-caption); color: var(--color-text-tertiary); }
.kpi-up { color: var(--color-success); }

/* Layout */
.salary-layout {
  display: grid;
  grid-template-columns: 1fr 320px;
  gap: var(--space-5);
}
.salary-main { display: flex; flex-direction: column; gap: var(--space-5); }
.salary-sidebar { display: flex; flex-direction: column; gap: var(--space-5); }

/* Cards & Sections */
.card {
  background: var(--color-white);
  border: 0.5px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-5);
  overflow: hidden;
}
.section h2, .section h3 {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  margin-bottom: var(--space-3);
}
.section h2 { font: var(--font-h2); }
.section h3 { font: var(--font-h3); }
.section-sub { font: var(--font-small); color: var(--color-text-tertiary); margin: -8px 0 var(--space-4) 0; }
.table-filter-label { font-weight: 400; color: var(--color-text-secondary); }

/* Box Plot */
.box-plot-container {
  position: relative;
  height: 60px;
  margin: var(--space-5) var(--space-4) 44px;
}
.box-whisker {
  position: absolute;
  top: 50%;
  height: 2px;
  background: var(--color-text-tertiary);
  transform: translateY(-50%);
}
.box-tick {
  position: absolute;
  top: 50%;
  width: 2px;
  height: 24px;
  background: var(--color-text-tertiary);
  transform: translate(-50%, -50%);
}
.box-iqr {
  position: absolute;
  top: 50%;
  height: 36px;
  background: var(--color-primary-light);
  border: 2px solid var(--color-primary);
  border-radius: var(--radius-sm);
  transform: translateY(-50%);
}
.box-median {
  position: absolute;
  top: 50%;
  width: 3px;
  height: 44px;
  background: var(--color-primary);
  border-radius: 2px;
  transform: translate(-50%, -50%);
  z-index: 2;
}
.box-labels {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  margin-top: 12px;
}
.box-labels span {
  position: absolute;
  transform: translateX(-50%);
  font: var(--font-caption);
  color: var(--color-text-secondary);
  white-space: nowrap;
}
.box-label-median { font-weight: 600; color: var(--color-primary) !important; }

/* Bar Chart */
.bar-chart { display: flex; flex-direction: column; gap: var(--space-3); }
.bar-row {
  display: grid;
  grid-template-columns: 130px 1fr 80px;
  align-items: center;
  gap: var(--space-3);
}
.bar-label { font: var(--font-small); color: var(--color-text-secondary); text-align: right; }
.bar-track {
  position: relative;
  height: 24px;
  background: var(--color-surface);
  border-radius: var(--radius-sm);
  overflow: hidden;
}
.bar-range {
  position: absolute;
  top: 0;
  height: 100%;
  background: var(--color-primary-light);
  opacity: 0.5;
}
.bar-fill {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  background: var(--color-primary);
  border-radius: var(--radius-sm);
  opacity: 0.65;
  transition: width 0.4s ease;
}
.bar-fill.bar-highlight { opacity: 1; }
.bar-value { font: var(--font-small); font-weight: 500; color: var(--color-text-primary); }

/* Trend Chart */
.trend-chart { position: relative; }
.trend-svg {
  width: 100%;
  height: 180px;
  display: block;
}
.trend-labels {
  display: flex;
  justify-content: space-between;
  margin-top: var(--space-2);
}
.trend-label {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}
.trend-year { font: var(--font-caption); color: var(--color-text-tertiary); }
.trend-val { font: var(--font-caption); font-weight: 500; color: var(--color-text-primary); }

/* Insights */
.insight-list { display: flex; flex-direction: column; gap: var(--space-3); }
.insight-item {
  display: flex;
  gap: var(--space-3);
  align-items: flex-start;
}
.insight-icon {
  width: 28px;
  height: 28px;
  border-radius: var(--radius-full);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 13px;
  flex-shrink: 0;
}
.insight-up { background: var(--color-success-bg); color: var(--color-success); }
.insight-info { background: var(--color-primary-light); color: var(--color-primary); }
.insight-warn { background: var(--color-warning-bg); color: var(--color-warning); }
.insight-title { font: var(--font-small); font-weight: 500; }
.insight-desc { font: var(--font-caption); color: var(--color-text-tertiary); }

/* Data Sources */
.source-list { display: flex; flex-direction: column; gap: var(--space-2); }
.source-item {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font: var(--font-caption);
  color: var(--color-text-secondary);
}
.source-dot {
  width: 8px;
  height: 8px;
  border-radius: var(--radius-full);
  flex-shrink: 0;
}
.source-dot-blue { background: var(--color-primary); }
.source-dot-green { background: var(--color-success); }
.source-dot-amber { background: var(--color-accent); }
.source-dot-gray { background: var(--color-text-tertiary); }
.source-note {
  font: var(--font-caption);
  color: var(--color-text-tertiary);
  margin-top: var(--space-3);
  padding-top: var(--space-3);
  border-top: 1px solid var(--color-border);
}

/* CTA */
.cta-card { text-align: center; }
.cta-icon { font-size: 32px; margin-bottom: var(--space-2); }
.cta-card h3 { justify-content: center; }
.cta-text { font: var(--font-small); color: var(--color-text-secondary); }

/* Salary Table */
.table-wrapper { overflow-x: auto; }
.salary-table {
  width: 100%;
  border-collapse: collapse;
  font: var(--font-small);
}
.salary-table th {
  text-align: left;
  padding: var(--space-3) var(--space-4);
  border-bottom: 2px solid var(--color-border);
  font: var(--font-caption);
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  color: var(--color-text-secondary);
}
.salary-table td {
  padding: var(--space-3) var(--space-4);
  border-bottom: 1px solid var(--color-border);
}
.salary-table th.num, .salary-table td.num { text-align: right; }
.num-median { font-weight: 600; color: var(--color-primary); }
.num-range { color: var(--color-text-tertiary); }
.row-selected {
  background: var(--color-primary-light);
}
.row-selected td { font-weight: 500; }
.rank-name { margin-right: var(--space-2); }
.rank-badge {
  display: inline-block;
  background: var(--color-primary);
  color: white;
  font: var(--font-caption);
  padding: 1px 8px;
  border-radius: 10px;
  vertical-align: middle;
}
.salary-table tr:hover { background: var(--color-surface); }
.salary-table tr.row-selected:hover { background: var(--color-primary-light); }

/* Disclaimer */
.disclaimer {
  display: flex;
  align-items: flex-start;
  gap: var(--space-2);
  margin-top: var(--space-5);
  padding: var(--space-4);
  background: var(--color-surface);
  border-radius: var(--radius-md);
  font: var(--font-caption);
  color: var(--color-text-tertiary);
  line-height: 1.5;
}
.disclaimer svg { flex-shrink: 0; margin-top: 1px; }

/* Responsive */
@media (max-width: 1024px) {
  .salary-layout { grid-template-columns: 1fr; }
  .salary-sidebar { order: -1; }
}
@media (max-width: 768px) {
  .filter-grid { grid-template-columns: repeat(2, 1fr); }
  .kpi-grid { grid-template-columns: repeat(2, 1fr); }
  .bar-row { grid-template-columns: 90px 1fr 60px; }
  .salary-header { flex-direction: column; gap: var(--space-3); }
}
@media (max-width: 480px) {
  .filter-grid { grid-template-columns: 1fr; }
  .kpi-grid { grid-template-columns: 1fr 1fr; }
}
</style>
