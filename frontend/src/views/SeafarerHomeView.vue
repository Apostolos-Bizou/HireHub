<script setup>
import { ref, computed } from 'vue'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const firstName = computed(() => auth.user?.fullName?.split(' ')[0] || 'Seafarer')

// Active demand tab
const demandView = ref('rank')
const expandedRank = ref(null)

// Demand data with top-rated companies per rank
const demandData = [
  { rank: 'Master', count: 28, trend: '+5 vs last week', highlight: false, companies: [
    { name: 'Stolt-Nielsen', initials: 'SN', color: '#534AB7', country: 'Norway', vessels: 40, crewRating: 4.7, openings: 5 },
    { name: 'Tsakos Energy', initials: 'TE', color: '#0A66C2', country: 'Greece', vessels: 78, crewRating: 4.5, openings: 4 }
  ]},
  { rank: 'Chief Officer', count: 34, trend: '+8 vs last week', highlight: false, companies: [
    { name: 'Capital Ship Mgmt', initials: 'CS', color: '#1D9E75', country: 'Greece', vessels: 92, crewRating: 4.4, openings: 6 },
    { name: 'Tsakos Energy', initials: 'TE', color: '#0A66C2', country: 'Greece', vessels: 78, crewRating: 4.5, openings: 5 }
  ]},
  { rank: '2nd Officer', count: 52, trend: '+12 vs last week', highlight: true, companies: [
    { name: 'Tsakos Energy', initials: 'TE', color: '#0A66C2', country: 'Greece', vessels: 78, crewRating: 4.5, openings: 8 },
    { name: 'Diana Shipping', initials: 'DS', color: '#E7A33E', country: 'Greece', vessels: 36, crewRating: 4.3, openings: 6 }
  ]},
  { rank: '3rd Officer', count: 67, trend: '+15 vs last week', highlight: true, companies: [
    { name: 'Tsakos Energy', initials: 'TE', color: '#0A66C2', country: 'Greece', vessels: 78, crewRating: 4.5, openings: 10 },
    { name: 'Capital Ship Mgmt', initials: 'CS', color: '#1D9E75', country: 'Greece', vessels: 92, crewRating: 4.4, openings: 8 }
  ]},
  { rank: 'Chief Engineer', count: 22, trend: 'Stable', highlight: false, companies: [
    { name: 'Stolt-Nielsen', initials: 'SN', color: '#534AB7', country: 'Norway', vessels: 40, crewRating: 4.7, openings: 3 },
    { name: 'Dorian LPG', initials: 'DL', color: '#D85A30', country: 'Greece', vessels: 22, crewRating: 4.2, openings: 2 }
  ]},
  { rank: '2nd Engineer', count: 38, trend: '+6 vs last week', highlight: false, companies: [
    { name: 'Capital Ship Mgmt', initials: 'CS', color: '#1D9E75', country: 'Greece', vessels: 92, crewRating: 4.4, openings: 5 },
    { name: 'Costamare', initials: 'CM', color: '#E7A33E', country: 'Greece', vessels: 114, crewRating: 4.1, openings: 4 }
  ]},
  { rank: '3rd Engineer', count: 41, trend: '+9 vs last week', highlight: false, companies: [
    { name: 'Tsakos Energy', initials: 'TE', color: '#0A66C2', country: 'Greece', vessels: 78, crewRating: 4.5, openings: 6 },
    { name: 'Varship Shipping', initials: 'VS', color: '#1D9E75', country: 'Greece', vessels: 3, crewRating: 4.6, openings: 2 }
  ]},
  { rank: 'Electrician', count: 18, trend: '+3 vs last week', highlight: false, companies: [
    { name: 'Stolt-Nielsen', initials: 'SN', color: '#534AB7', country: 'Norway', vessels: 40, crewRating: 4.7, openings: 2 },
    { name: 'Capital Ship Mgmt', initials: 'CS', color: '#1D9E75', country: 'Greece', vessels: 92, crewRating: 4.4, openings: 2 }
  ]},
  { rank: 'Bosun', count: 24, trend: 'Stable', highlight: false, companies: [
    { name: 'Diana Shipping', initials: 'DS', color: '#E7A33E', country: 'Greece', vessels: 36, crewRating: 4.3, openings: 4 },
    { name: 'Costamare', initials: 'CM', color: '#E7A33E', country: 'Greece', vessels: 114, crewRating: 4.1, openings: 3 }
  ]},
  { rank: 'AB Seaman', count: 56, trend: '+11 vs last week', highlight: true, companies: [
    { name: 'Capital Ship Mgmt', initials: 'CS', color: '#1D9E75', country: 'Greece', vessels: 92, crewRating: 4.4, openings: 9 },
    { name: 'Tsakos Energy', initials: 'TE', color: '#0A66C2', country: 'Greece', vessels: 78, crewRating: 4.5, openings: 7 }
  ]}
]
</script>

<template>
  <div class="container seafarer-dash">
    <!-- Welcome Header -->
    <div class="dash-header">
      <div>
        <h1>Welcome back, {{ firstName }}</h1>
        <p class="dash-sub">Maritime job market overview · Updated today</p>
      </div>
      <div class="dash-header-actions">
        <router-link to="/salary" class="btn btn-primary">Salary Benchmark</router-link>
        <router-link to="/profile" class="btn btn-secondary">My Profile</router-link>
      </div>
    </div>

    <!-- KPI Row 1 — Market Overview -->
    <div class="kpi-grid">
      <div class="kpi-card">
        <div class="kpi-label" title="Ανοιχτές θέσεις τώρα στην πλατφόρμα">Open positions</div>
        <div class="kpi-val kpi-blue">342</div>
        <div class="kpi-trend kpi-up">+47 this week</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label" title="Εταιρείες που αναζητούν πλήρωμα αυτή τη στιγμή">Companies hiring</div>
        <div class="kpi-val kpi-amber">86</div>
        <div class="kpi-trend kpi-up">+12 this month</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label" title="Μέσος μισθός Officers στην αγορά">Avg Officer salary</div>
        <div class="kpi-val kpi-green">$5,840</div>
        <div class="kpi-trend kpi-up">+4.2% YoY</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label" title="Πόσοι ναυτικοί βρήκαν εργασία μέσω HireHub αυτόν τον μήνα">Placements (MTD)</div>
        <div class="kpi-val">278</div>
        <div class="kpi-trend kpi-up">+15% vs last month</div>
      </div>
    </div>

    <!-- KPI Row 2 — Your Profile Stats -->
    <div class="kpi-grid">
      <div class="kpi-card">
        <div class="kpi-label" title="Πόσες φορές είδαν το προφίλ σου πλοιοκτήτες">My profile views</div>
        <div class="kpi-val">347</div>
        <div class="kpi-trend kpi-up">+89 this month</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label" title="Σε πόσες shortlists εμφανίζεσαι">On shortlists</div>
        <div class="kpi-val kpi-blue">12</div>
        <div class="kpi-trend kpi-up">+3 this week</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label" title="Ερωτήσεις από πλοιοκτήτες / agents">Inquiries received</div>
        <div class="kpi-val kpi-amber">5</div>
        <div class="kpi-trend">2 unread</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label" title="Η βαθμολογία σου από αξιολογήσεις συναδέλφων">My CrewScore</div>
        <div class="kpi-val kpi-green">82</div>
        <div class="kpi-trend kpi-up">+4 vs last quarter</div>
      </div>
    </div>

    <!-- Main Layout -->
    <div class="dash-layout">
      <div class="dash-main">
        <!-- Demand by Rank -->
        <div class="card section">
          <div class="section-header">
            <h2>
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="12" width="4" height="9"/><rect x="10" y="7" width="4" height="14"/><rect x="17" y="3" width="4" height="18"/></svg>
              Market demand — open positions by rank
            </h2>
            <span class="section-tag" title="Τελευταία ενημέρωση σήμερα">Live data</span>
          </div>
          <p class="section-sub" title="Κλικ σε βαθμό για τις καλύτερες εταιρείες">Click on a rank to see top-rated companies hiring · Ratings by seafarers</p>
          <div class="demand-grid">
            <div v-for="d in demandData" :key="d.rank" class="demand-card" :class="{ highlight: d.highlight, expanded: expandedRank === d.rank }" @click="expandedRank = expandedRank === d.rank ? null : d.rank">
              <div class="demand-rank">{{ d.rank }}</div>
              <div class="demand-count">{{ d.count }}</div>
              <div class="demand-bar"><div class="demand-fill" :class="{ 'demand-fill-high': d.highlight }" :style="{ width: (d.count / 67 * 100) + '%' }"></div></div>
              <div class="demand-trend" :class="{ 'kpi-up': d.trend.startsWith('+') }">{{ d.trend }}</div>
              <!-- Expanded: Top 2 companies -->
              <div v-if="expandedRank === d.rank" class="demand-companies">
                <div class="dc-label">Top rated companies hiring {{ d.rank }}s:</div>
                <div v-for="c in d.companies" :key="c.name" class="dc-company">
                  <div class="dc-avatar" :style="{ background: c.color }">{{ c.initials }}</div>
                  <div class="dc-info">
                    <strong>{{ c.name }}</strong>
                    <span>{{ c.country }} · {{ c.vessels }} vessels</span>
                  </div>
                  <div class="dc-scores">
                    <div class="dc-score dc-score-green" :title="'Crew rating ' + c.crewRating + '/5 by seafarers'">{{ c.crewRating }}</div>
                    <div class="dc-score dc-score-blue" :title="c.openings + ' open positions'">{{ c.openings }}</div>
                  </div>
                </div>
                <router-link :to="'/search?rank=' + d.rank" class="dc-view-all">View all {{ d.count }} {{ d.rank }}s</router-link>
              </div>
            </div>
          </div>
        </div>

        <!-- Top Hiring Companies -->
        <div class="card section">
          <h2>
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M2 20h20"/><path d="M5 20V8l7-5 7 5v12"/><path d="M9 20v-6h6v6"/></svg>
            Top hiring companies
          </h2>
          <p class="section-sub" title="Εταιρείες με τις περισσότερες ανοιχτές θέσεις τώρα">Companies with the most active openings on HireHub</p>
          <div class="company-list">
            <div class="company-row">
              <div class="company-avatar avatar-owner">TE</div>
              <div class="company-info">
                <div class="company-name">Tsakos Energy Navigation</div>
                <div class="company-meta">Greece · 78 vessels · Oil/Chemical Tanker</div>
              </div>
              <div class="company-openings">
                <span class="opening-count">18</span>
                <span class="opening-label">openings</span>
              </div>
            </div>
            <div class="company-row">
              <div class="company-avatar avatar-owner">CS</div>
              <div class="company-info">
                <div class="company-name">Capital Ship Management</div>
                <div class="company-meta">Greece · 92 vessels · Tanker & Bulk</div>
              </div>
              <div class="company-openings">
                <span class="opening-count">15</span>
                <span class="opening-label">openings</span>
              </div>
            </div>
            <div class="company-row">
              <div class="company-avatar avatar-owner">DS</div>
              <div class="company-info">
                <div class="company-name">Diana Shipping</div>
                <div class="company-meta">Greece · 36 vessels · Bulk Carrier</div>
              </div>
              <div class="company-openings">
                <span class="opening-count">12</span>
                <span class="opening-label">openings</span>
              </div>
            </div>
            <div class="company-row">
              <div class="company-avatar avatar-owner">DL</div>
              <div class="company-info">
                <div class="company-name">Dorian LPG</div>
                <div class="company-meta">Greece · 22 vessels · LPG/VLGC</div>
              </div>
              <div class="company-openings">
                <span class="opening-count">9</span>
                <span class="opening-label">openings</span>
              </div>
            </div>
            <div class="company-row">
              <div class="company-avatar avatar-owner">CM</div>
              <div class="company-info">
                <div class="company-name">Costamare</div>
                <div class="company-meta">Greece · 114 vessels · Container</div>
              </div>
              <div class="company-openings">
                <span class="opening-count">8</span>
                <span class="opening-label">openings</span>
              </div>
            </div>
            <div class="company-row">
              <div class="company-avatar avatar-owner">VS</div>
              <div class="company-info">
                <div class="company-name">Varship Shipping Co.</div>
                <div class="company-meta">Greece · 3 vessels · Oil/Chemical Tanker</div>
              </div>
              <div class="company-openings">
                <span class="opening-count">4</span>
                <span class="opening-label">openings</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Charts Row -->
        <div class="charts-row">
          <div class="card section chart-card">
            <h2 title="Εξέλιξη ανοιχτών θέσεων — τελευταίοι 6 μήνες">Openings trend</h2>
            <canvas id="openingsTrend" style="width:100%;height:200px;"></canvas>
          </div>
          <div class="card section chart-card">
            <h2 title="Ζήτηση ανά τύπο πλοίου">Demand by vessel type</h2>
            <canvas id="vesselDemand" style="width:100%;height:200px;"></canvas>
          </div>
        </div>
      </div>

      <!-- Right Sidebar -->
      <aside class="dash-sidebar">
        <!-- Profile Completeness -->
        <div class="card section">
          <h3>
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
            Profile completeness
          </h3>
          <div class="profile-progress">
            <div class="progress-bar-bg"><div class="progress-bar-fill" style="width:92%"></div></div>
            <span class="progress-pct">92%</span>
          </div>
          <div class="profile-checklist">
            <div class="check-item done"><span class="check-icon">✓</span> Personal information</div>
            <div class="check-item done"><span class="check-icon">✓</span> Sea service records (6)</div>
            <div class="check-item done"><span class="check-icon">✓</span> Certificates uploaded (10)</div>
            <div class="check-item done"><span class="check-icon">✓</span> Skills & endorsements</div>
            <div class="check-item done"><span class="check-icon">✓</span> Availability set</div>
            <div class="check-item pending"><span class="check-icon">○</span> Add profile photo</div>
          </div>
          <router-link to="/profile" class="btn btn-secondary" style="width:100%;text-align:center;margin-top:var(--space-3);font-size:12px;">Complete my profile</router-link>
        </div>

        <!-- Alerts -->
        <div class="card section">
          <h3>
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/></svg>
            Alerts
          </h3>
          <div class="alert-list">
            <div class="alert-item">
              <span class="alert-badge alert-urgent">Urgent</span>
              <p>Your STCW Basic Safety expires in 30 days</p>
            </div>
            <div class="alert-item">
              <span class="alert-badge alert-new">New</span>
              <p>Tsakos Energy viewed your profile</p>
            </div>
            <div class="alert-item">
              <span class="alert-badge alert-new">New</span>
              <p>Added to shortlist: 3rd Officer — MT Pacific</p>
            </div>
            <div class="alert-item">
              <span class="alert-badge alert-info">Info</span>
              <p>New CrewScore review from Chief Officer</p>
            </div>
            <div class="alert-item">
              <span class="alert-badge alert-info">Info</span>
              <p>67 new 3rd Officer positions this week</p>
            </div>
          </div>
        </div>

        <!-- Review Colleagues -->
        <div class="card section">
          <h3>
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
            Review colleagues
          </h3>
          <p class="review-hint" title="Αξιολόγησε συναδέλφους που ήταν μαζί σου στο ίδιο πλοίο">Seafarers you served with — tap to review</p>
          <div class="colleague-list">
            <router-link to="/seafarer/106" class="colleague-row">
              <div class="colleague-avatar" style="background:#0A66C2">DP</div>
              <div class="colleague-info">
                <strong>Dmitry Petrov</strong>
                <span>Chief Engineer · MT Aegean Warrior · 8 months</span>
              </div>
              <span class="colleague-action">Review</span>
            </router-link>
            <router-link to="/seafarer/102" class="colleague-row">
              <div class="colleague-avatar" style="background:#E7A33E">RG</div>
              <div class="colleague-info">
                <strong>Ruslan Goncharov</strong>
                <span>2nd Officer · MT Mediterranean Star · 6 months</span>
              </div>
              <span class="colleague-action">Review</span>
            </router-link>
            <router-link to="/seafarer/103" class="colleague-row">
              <div class="colleague-avatar" style="background:#1D9E75">AM</div>
              <div class="colleague-info">
                <strong>Andrei Marinescu</strong>
                <span>Chief Officer · MT Coral Bay · 4 months</span>
              </div>
              <span class="colleague-action">Review</span>
            </router-link>
          </div>
        </div>

        <!-- Salary Snapshot -->
        <div class="card section">
          <h3>
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="1" x2="12" y2="23"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg>
            Salary snapshot — 3rd Officer
          </h3>
          <div class="salary-mini">
            <div class="salary-mini-row"><span>Market low</span><strong>$2,200</strong></div>
            <div class="salary-mini-row"><span>Median</span><strong class="salary-highlight">$3,400</strong></div>
            <div class="salary-mini-row"><span>Market high</span><strong>$5,200</strong></div>
            <div class="salary-mini-row"><span>YoY change</span><strong class="kpi-up">+4.0%</strong></div>
          </div>
          <router-link to="/salary" class="btn btn-secondary" style="width:100%;text-align:center;margin-top:var(--space-3);font-size:12px;">Full salary benchmark</router-link>
        </div>

        <!-- Quick Actions -->
        <div class="card section">
          <h3 title="Γρήγορες ενέργειες">Quick actions</h3>
          <div class="quick-actions">
            <router-link to="/profile" class="qa-btn">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
              My profile
            </router-link>
            <router-link to="/salary" class="qa-btn">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="1" x2="12" y2="23"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg>
              Salary benchmark
            </router-link>
            <router-link to="/messages" class="qa-btn">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
              Messages (2 unread)
            </router-link>
            <router-link to="/guide" class="qa-btn">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/><path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/></svg>
              Platform guide
            </router-link>
          </div>
        </div>
      </aside>
    </div>
  </div>
</template>

<script>
export default {
  mounted() {
    this.renderCharts()
  },
  methods: {
    async renderCharts() {
      const script = document.createElement('script')
      script.src = 'https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.js'
      script.onload = () => {
        new window.Chart(document.getElementById('openingsTrend'), {
          type: 'line',
          data: {
            labels: ['Nov','Dec','Jan','Feb','Mar','Apr'],
            datasets: [
              { label: 'Officer positions', data: [185,210,238,265,298,342], borderColor: '#0A66C2', backgroundColor: 'rgba(10,102,194,0.08)', fill: true, tension: 0.3, pointRadius: 3 },
              { label: 'Rating positions', data: [120,135,142,158,178,195], borderColor: '#E7A33E', backgroundColor: 'transparent', borderDash: [4,4], tension: 0.3, pointRadius: 3 }
            ]
          },
          options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'bottom', labels: { boxWidth: 10, font: { size: 11 } } } }, scales: { y: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.05)' } }, x: { grid: { display: false } } } }
        })
        new window.Chart(document.getElementById('vesselDemand'), {
          type: 'doughnut',
          data: {
            labels: ['Oil/Chem Tanker','Bulk Carrier','Container','LPG/LNG','Offshore','Other'],
            datasets: [{ data: [34,21,17,12,9,7], backgroundColor: ['#0A66C2','#E7A33E','#1D9E75','#534AB7','#D85A30','#888780'] }]
          },
          options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'bottom', labels: { boxWidth: 10, font: { size: 11 }, padding: 8 } } }, cutout: '55%' }
        })
      }
      document.head.appendChild(script)
    }
  }
}
</script>

<style scoped>
.seafarer-dash { padding-top: var(--space-5); padding-bottom: var(--space-8); }

.dash-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--space-4); }
.dash-header h1 { font: var(--font-h1); }
.dash-sub { font: var(--font-small); color: var(--color-text-secondary); margin-top: 2px; }
.dash-header-actions { display: flex; gap: var(--space-2); }

/* KPI Grid */
.kpi-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: var(--space-3); margin-bottom: var(--space-3); }
.kpi-card {
  background: var(--color-white);
  border-radius: var(--radius-lg);
  padding: var(--space-4);
  border: 1px solid var(--color-border);
  box-shadow: 0 2px 8px rgba(0,0,0,0.06), 0 4px 16px rgba(0,0,0,0.04);
  transition: transform 0.15s, box-shadow 0.15s;
}
.kpi-card:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.08), 0 8px 24px rgba(0,0,0,0.06); }
.kpi-label { font: var(--font-small); color: var(--color-text-secondary); margin-bottom: 6px; }
.kpi-val { font-size: 28px; font-weight: 600; }
.kpi-blue { color: var(--color-primary); }
.kpi-amber { color: var(--color-accent); }
.kpi-green { color: var(--color-success); }
.kpi-trend { font: var(--font-caption); color: var(--color-text-tertiary); margin-top: 6px; }
.kpi-up { color: var(--color-success); }

/* Layout */
.dash-layout { display: grid; grid-template-columns: 1fr 300px; gap: var(--space-4); margin-top: var(--space-4); }
.dash-main { min-width: 0; }

.section { padding: var(--space-4); margin-bottom: var(--space-3); }
.section h2 { font: var(--font-h2); margin-bottom: var(--space-3); display: flex; align-items: center; gap: var(--space-2); }
.section h3 { font: var(--font-h3); margin-bottom: var(--space-3); display: flex; align-items: center; gap: var(--space-2); }
.section-header { display: flex; justify-content: space-between; align-items: flex-start; }
.section-tag { font: var(--font-caption); font-weight: 500; color: var(--color-success); background: var(--color-success-bg); padding: 3px 10px; border-radius: 10px; }
.section-sub { font: var(--font-small); color: var(--color-text-tertiary); margin: -8px 0 var(--space-4) 0; }

/* Demand Grid */
.demand-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: var(--space-3); }
.demand-card {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: var(--space-3) var(--space-4);
  transition: background 0.15s;
  cursor: pointer;
}
.demand-card:hover { background: var(--color-primary-light); }
.demand-card.highlight { border-left: 3px solid var(--color-primary); }
.demand-card.expanded { background: var(--color-primary-light); border: 1.5px solid var(--color-primary); grid-column: span 2; }
.demand-rank { font: var(--font-small); font-weight: 500; }
.demand-count { font-size: 22px; font-weight: 600; color: var(--color-primary); margin: 2px 0; }
.demand-bar { height: 4px; background: var(--color-border); border-radius: 2px; margin: 6px 0; }
.demand-fill { height: 100%; background: var(--color-primary); border-radius: 2px; transition: width 0.4s ease; opacity: 0.6; }
.demand-fill-high { opacity: 1; background: var(--color-primary); }
.demand-trend { font: var(--font-caption); color: var(--color-text-tertiary); }

/* Expanded companies */
.demand-companies { margin-top: var(--space-3); padding-top: var(--space-3); border-top: 1px solid var(--color-border); }
.dc-label { font: var(--font-caption); font-weight: 500; color: var(--color-text-secondary); margin-bottom: var(--space-2); }
.dc-company {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) 0;
  border-bottom: 1px solid rgba(0,0,0,0.06);
}
.dc-company:last-of-type { border-bottom: none; }
.dc-avatar {
  width: 32px; height: 32px; border-radius: var(--radius-sm);
  display: flex; align-items: center; justify-content: center;
  color: white; font-weight: 600; font-size: 11px; flex-shrink: 0;
}
.dc-info { flex: 1; min-width: 0; }
.dc-info strong { display: block; font: var(--font-small); font-weight: 500; }
.dc-info span { font: var(--font-caption); color: var(--color-text-tertiary); }
.dc-scores { display: flex; gap: 4px; flex-shrink: 0; }
.dc-score {
  width: 30px; height: 30px; border-radius: 50%; border: 2px solid;
  display: flex; align-items: center; justify-content: center;
  font-size: 11px; font-weight: 600;
}
.dc-score-green { border-color: var(--color-success); color: var(--color-success); }
.dc-score-blue { border-color: var(--color-primary); color: var(--color-primary); }
.dc-view-all {
  display: block; text-align: center; margin-top: var(--space-2);
  font: var(--font-caption); font-weight: 500; color: var(--color-primary);
  padding: var(--space-2); border: 1px solid var(--color-primary);
  border-radius: var(--radius-md); text-decoration: none;
  transition: background 0.15s;
}
.dc-view-all:hover { background: var(--color-white); text-decoration: none; }

/* Company List */
.company-list { display: flex; flex-direction: column; }
.company-row {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3) 0;
  border-bottom: 1px solid var(--color-border);
  transition: background 0.15s;
}
.company-row:last-child { border-bottom: none; }
.company-row:hover { background: var(--color-surface); margin: 0 calc(-1 * var(--space-4)); padding-left: var(--space-4); padding-right: var(--space-4); }
.company-avatar {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 13px;
  flex-shrink: 0;
  background: var(--color-accent-light);
  color: var(--color-accent);
}
.company-info { flex: 1; min-width: 0; }
.company-name { font: var(--font-body); font-weight: 500; }
.company-meta { font: var(--font-caption); color: var(--color-text-tertiary); }
.company-openings { text-align: right; flex-shrink: 0; }
.opening-count { display: block; font-size: 20px; font-weight: 600; color: var(--color-primary); }
.opening-label { font: var(--font-caption); color: var(--color-text-tertiary); }

/* Charts */
.charts-row { display: grid; grid-template-columns: 1fr 1fr; gap: var(--space-3); }
.chart-card canvas { max-height: 200px; }

/* Sidebar */
.dash-sidebar { position: sticky; top: 76px; align-self: start; }

/* Profile Progress */
.profile-progress { display: flex; align-items: center; gap: var(--space-3); margin-bottom: var(--space-3); }
.progress-bar-bg { flex: 1; height: 8px; background: var(--color-surface); border-radius: 4px; overflow: hidden; }
.progress-bar-fill { height: 100%; background: var(--color-success); border-radius: 4px; }
.progress-pct { font: var(--font-small); font-weight: 600; color: var(--color-success); }
.profile-checklist { display: flex; flex-direction: column; gap: var(--space-1); }
.check-item { font: var(--font-caption); color: var(--color-text-secondary); display: flex; align-items: center; gap: var(--space-2); }
.check-item.done { color: var(--color-success); }
.check-item.pending { color: var(--color-warning); }
.check-icon { width: 16px; text-align: center; font-weight: 600; }

/* Alerts */
.alert-list { display: flex; flex-direction: column; gap: var(--space-2); }
.alert-item { padding: var(--space-2) 0; border-bottom: 1px solid var(--color-border); }
.alert-item:last-child { border-bottom: none; }
.alert-badge { font: var(--font-caption); font-weight: 500; padding: 2px 6px; border-radius: 4px; display: inline-block; margin-bottom: 2px; }
.alert-urgent { background: var(--color-danger-bg); color: var(--color-danger); }
.alert-new { background: var(--color-primary-light); color: var(--color-primary); }
.alert-info { background: var(--color-surface); color: var(--color-text-secondary); }
.alert-item p { font: var(--font-caption); color: var(--color-text-secondary); }

/* Salary Mini */
.salary-mini { display: flex; flex-direction: column; }
.salary-mini-row { display: flex; justify-content: space-between; padding: var(--space-2) 0; border-bottom: 1px solid var(--color-border); font: var(--font-small); }
.salary-mini-row:last-child { border-bottom: none; }
.salary-mini-row span { color: var(--color-text-secondary); }
.salary-highlight { color: var(--color-primary); }

/* Quick Actions */
.quick-actions { display: flex; flex-direction: column; gap: var(--space-2); }
.qa-btn {
  display: flex; align-items: center; gap: var(--space-2);
  padding: var(--space-2) var(--space-3);
  border: 1px solid var(--color-border); border-radius: var(--radius-md);
  font: var(--font-small); font-weight: 500;
  color: var(--color-text-primary); text-decoration: none;
  transition: border-color 0.15s, background 0.15s;
}
.qa-btn:hover { border-color: var(--color-primary); background: var(--color-primary-light); color: var(--color-primary); text-decoration: none; }

[title]{cursor:help}

/* Review Colleagues */
.review-hint { font: var(--font-caption); color: var(--color-text-tertiary); margin: -8px 0 var(--space-3) 0; }
.colleague-list { display: flex; flex-direction: column; gap: var(--space-1); }
.colleague-row {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2);
  border-radius: var(--radius-md);
  text-decoration: none;
  color: inherit;
  transition: background 0.15s;
}
.colleague-row:hover { background: var(--color-primary-light); text-decoration: none; }
.colleague-avatar {
  width: 32px; height: 32px; border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  color: white; font-weight: 600; font-size: 11px; flex-shrink: 0;
}
.colleague-info { flex: 1; min-width: 0; }
.colleague-info strong { display: block; font: var(--font-small); font-weight: 500; }
.colleague-info span { font: var(--font-caption); color: var(--color-text-tertiary); }
.colleague-action {
  font: var(--font-caption); font-weight: 500;
  color: var(--color-primary); background: var(--color-primary-light);
  padding: 3px 10px; border-radius: 10px; flex-shrink: 0;
}
@media (max-width: 1024px) {
  .dash-layout { grid-template-columns: 1fr; }
  .charts-row { grid-template-columns: 1fr; }
}
@media (max-width: 768px) {
  .kpi-grid { grid-template-columns: repeat(2, 1fr); }
  .demand-grid { grid-template-columns: 1fr; }
  .dash-header { flex-direction: column; gap: var(--space-3); align-items: flex-start; }
}
</style>
