<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import CrewAvailabilityTimeline from '@/components/crewscore/CrewAvailabilityTimeline.vue'

const auth = useAuthStore()
</script>

<template>
  <div class="container owner-dash">
    <!-- Welcome Header -->
    <div class="dash-header">
      <div>
        <h1>Welcome back, {{ auth.user?.fullName?.split(' ')[0] }}</h1>
        <p class="dash-sub">HireHub platform overview · Updated today</p>
      </div>
      <div class="dash-header-actions">
        <router-link to="/search" class="btn btn-primary">Search Talent</router-link>
        <router-link to="/ai-recommender" class="btn btn-secondary">AI Recommender</router-link>
      </div>
    </div>

    <!-- KPI Row 1 - Platform Overview -->
    <div class="kpi-grid">
      <div class="kpi-card">
        <div class="kpi-label">Registered seafarers</div>
        <div class="kpi-val kpi-blue">24,847</div>
        <div class="kpi-trend kpi-up">+1,230 this month</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label">Verified profiles</div>
        <div class="kpi-val kpi-amber">18,290</div>
        <div class="kpi-trend">73.6% verification rate</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label">Avg CrewScore</div>
        <div class="kpi-val kpi-green">78</div>
        <div class="kpi-trend kpi-up">+2.4 vs Q1</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label">Peer reviews total</div>
        <div class="kpi-val kpi-blue">12,340</div>
        <div class="kpi-trend kpi-up">+890 this month</div>
      </div>
    </div>

    <!-- KPI Row 2 - Your Activity -->
    <div class="kpi-grid">
      <div class="kpi-card">
        <div class="kpi-label">Your shortlists (MTD)</div>
        <div class="kpi-val">12</div>
        <div class="kpi-trend kpi-up">+4 vs last month</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label">Candidates forwarded</div>
        <div class="kpi-val">38</div>
        <div class="kpi-trend kpi-up">+15% vs last month</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label">Successful placements</div>
        <div class="kpi-val kpi-green">24</div>
        <div class="kpi-trend">89% success rate</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-label">Avg time to hire</div>
        <div class="kpi-val">6.2 days</div>
        <div class="kpi-trend kpi-up">-1.8 days vs Q1</div>
      </div>
    </div>

    <!-- Main Content: 2 columns -->
    <div class="dash-layout">
      <div class="dash-main">
        <!-- Crew Availability Timeline -->
        <div class="card section">
          <h2>
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
            Crew availability timeline
          </h2>
          <CrewAvailabilityTimeline />
        </div>

        <!-- Crew Availability & Nationalities Panels -->
        <div class="panels-row">
          <div class="card section">
            <h2>Crew availability by rank</h2>
            <div class="avail-list">
              <div class="avail-row"><span class="avail-rank">Master</span><div class="avail-bar-wrap"><div class="avail-bar" style="width:28%"></div></div><span class="avail-count">340</span></div>
              <div class="avail-row"><span class="avail-rank">Chief Officer</span><div class="avail-bar-wrap"><div class="avail-bar" style="width:42%"></div></div><span class="avail-count">512</span></div>
              <div class="avail-row"><span class="avail-rank">2nd Officer</span><div class="avail-bar-wrap"><div class="avail-bar" style="width:55%"></div></div><span class="avail-count">670</span></div>
              <div class="avail-row"><span class="avail-rank">3rd Officer</span><div class="avail-bar-wrap"><div class="avail-bar" style="width:70%"></div></div><span class="avail-count">852</span></div>
              <div class="avail-row"><span class="avail-rank">Chief Engineer</span><div class="avail-bar-wrap"><div class="avail-bar" style="width:22%"></div></div><span class="avail-count">268</span></div>
              <div class="avail-row"><span class="avail-rank">2nd Engineer</span><div class="avail-bar-wrap"><div class="avail-bar" style="width:38%"></div></div><span class="avail-count">462</span></div>
              <div class="avail-row"><span class="avail-rank">3rd Engineer</span><div class="avail-bar-wrap"><div class="avail-bar" style="width:48%"></div></div><span class="avail-count">584</span></div>
              <div class="avail-row"><span class="avail-rank">Bosun</span><div class="avail-bar-wrap"><div class="avail-bar" style="width:35%"></div></div><span class="avail-count">428</span></div>
              <div class="avail-row"><span class="avail-rank">AB</span><div class="avail-bar-wrap"><div class="avail-bar" style="width:85%"></div></div><span class="avail-count">1,036</span></div>
              <div class="avail-row"><span class="avail-rank">Oiler</span><div class="avail-bar-wrap"><div class="avail-bar" style="width:60%"></div></div><span class="avail-count">732</span></div>
            </div>
          </div>
          <div class="card section">
            <h2>Top nationalities</h2>
            <div class="nat-list">
              <div class="nat-row2"><span class="nat-name2">Filipino</span><span class="nat-count2">14,908</span><span class="nat-pct2">60%</span></div>
              <div class="nat-row2"><span class="nat-name2">Ukrainian</span><span class="nat-count2">2,732</span><span class="nat-pct2">11%</span></div>
              <div class="nat-row2"><span class="nat-name2">Indian</span><span class="nat-count2">1,988</span><span class="nat-pct2">8%</span></div>
              <div class="nat-row2"><span class="nat-name2">Romanian</span><span class="nat-count2">1,242</span><span class="nat-pct2">5%</span></div>
              <div class="nat-row2"><span class="nat-name2">Russian</span><span class="nat-count2">994</span><span class="nat-pct2">4%</span></div>
              <div class="nat-row2"><span class="nat-name2">Greek</span><span class="nat-count2">746</span><span class="nat-pct2">3%</span></div>
              <div class="nat-row2"><span class="nat-name2">Bulgarian</span><span class="nat-count2">497</span><span class="nat-pct2">2%</span></div>
              <div class="nat-row2"><span class="nat-name2">Indonesian</span><span class="nat-count2">373</span><span class="nat-pct2">1.5%</span></div>
              <div class="nat-row2"><span class="nat-name2">Chinese</span><span class="nat-count2">249</span><span class="nat-pct2">1%</span></div>
              <div class="nat-row2"><span class="nat-name2">Other</span><span class="nat-count2">1,118</span><span class="nat-pct2">4.5%</span></div>
            </div>
          </div>
        </div>

        <!-- Charts Row -->
        <div class="charts-row">
          <div class="card section chart-card">
            <h2>Monthly placements</h2>
            <canvas id="placementChart" style="width:100%;height:200px;"></canvas>
          </div>
          <div class="card section chart-card">
            <h2>Vessel type demand</h2>
            <canvas id="vesselChart" style="width:100%;height:200px;"></canvas>
          </div>
        </div>
      </div>

      <!-- Right Sidebar -->
      <aside class="dash-sidebar">
        <!-- Alerts -->
        <div class="card section">
          <h3>
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/></svg>
            Alerts
          </h3>
          <div class="alert-list">
            <div class="alert-item">
              <span class="alert-badge alert-urgent">Urgent</span>
              <p>3 crew positions expiring in 7 days</p>
            </div>
            <div class="alert-item">
              <span class="alert-badge alert-urgent">Urgent</span>
              <p>MT Varship Glory needs 3rd Officer by May 15</p>
            </div>
            <div class="alert-item">
              <span class="alert-badge alert-info">New</span>
              <p>12 new verified profiles match your criteria</p>
            </div>
            <div class="alert-item">
              <span class="alert-badge alert-info">Info</span>
              <p>Magsaysay Maritime: 2 candidates deployed</p>
            </div>
          </div>
        </div>

        <!-- Platform Stats -->
        <div class="card section">
          <h3>Platform performance</h3>
          <div class="stat-list">
            <div class="stat-row"><span>Profile views (MTD)</span><strong>48,320</strong></div>
            <div class="stat-row"><span>Search queries</span><strong>12,847</strong></div>
            <div class="stat-row"><span>AI match accuracy</span><strong class="stat-green">87%</strong></div>
            <div class="stat-row"><span>Manning agents active</span><strong>84</strong></div>
            <div class="stat-row"><span>DMW-verified agents</span><strong class="stat-green">100%</strong></div>
          </div>
        </div>

        <!-- Quick Actions -->
        <div class="card section">
          <h3>Quick actions</h3>
          <div class="quick-actions">
            <router-link to="/search" class="qa-btn">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
              Search talent
            </router-link>
            <router-link to="/ai-recommender" class="qa-btn">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2a4 4 0 0 1 4 4c0 1.95-1.4 3.58-3.25 3.93L12 22"/><path d="M12 2a4 4 0 0 0-4 4c0 1.95 1.4 3.58 3.25 3.93"/><circle cx="12" cy="6" r="1.5"/></svg>
              AI recommender
            </router-link>
            <router-link to="/shortlists" class="qa-btn">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"/><rect x="8" y="2" width="8" height="4" rx="1" ry="1"/></svg>
              View shortlists
            </router-link>
            <router-link to="/messages" class="qa-btn">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
              Messages
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
        new window.Chart(document.getElementById('placementChart'), {
          type: 'line',
          data: {
            labels: ['Oct','Nov','Dec','Jan','Feb','Mar','Apr'],
            datasets: [
              { label: 'Placements', data: [142,168,155,189,210,245,278], borderColor: '#0A66C2', backgroundColor: 'rgba(10,102,194,0.08)', fill: true, tension: 0.3, pointRadius: 3 },
              { label: 'Shortlists', data: [310,342,298,380,420,465,512], borderColor: '#E7A33E', backgroundColor: 'transparent', borderDash: [4,4], tension: 0.3, pointRadius: 3 }
            ]
          },
          options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'bottom', labels: { boxWidth: 10, font: { size: 11 } } } }, scales: { y: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.05)' } }, x: { grid: { display: false } } } }
        })
        new window.Chart(document.getElementById('vesselChart'), {
          type: 'doughnut',
          data: {
            labels: ['Oil tanker','Bulk carrier','Container','Chemical','LPG/LNG','Other'],
            datasets: [{ data: [32,22,18,14,8,6], backgroundColor: ['#0A66C2','#E7A33E','#1D9E75','#534AB7','#D85A30','#888780'] }]
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
.owner-dash {
  padding-top: var(--space-5);
  padding-bottom: var(--space-8);
}

/* Header */
.dash-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-4);
}
.dash-header h1 { font: var(--font-h1); }
.dash-sub { font: var(--font-small); color: var(--color-text-secondary); margin-top: 2px; }
.dash-header-actions { display: flex; gap: var(--space-2); }

/* KPI Grid */
.kpi-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-3);
  margin-bottom: var(--space-3);
}
.kpi-card {
  background: var(--color-white); border-radius: var(--radius-lg); padding: var(--space-4); border: 1px solid var(--color-border); box-shadow: 0 2px 8px rgba(0,0,0,0.06), 0 4px 16px rgba(0,0,0,0.04);
}
.kpi-label { font: var(--font-caption); color: var(--color-text-secondary); margin-bottom: 4px; }
.kpi-val { font-size: 24px; font-weight: 600; }
.kpi-blue { color: var(--color-primary); }
.kpi-amber { color: var(--color-accent); }
.kpi-green { color: var(--color-success); }
.kpi-trend { font: var(--font-caption); color: var(--color-text-tertiary); margin-top: 4px; }
.kpi-up { color: var(--color-success); }

/* Main Layout */
.dash-layout {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: var(--space-4);
  margin-top: var(--space-4);
}
.dash-main { min-width: 0; }

.section { padding: var(--space-4); margin-bottom: var(--space-3); box-shadow: 0 2px 8px rgba(0,0,0,0.06), 0 4px 16px rgba(0,0,0,0.04); }
.section h2 {
  font: var(--font-h2);
  margin-bottom: var(--space-3);
  display: flex;
  align-items: center;
  gap: var(--space-2);
}
.section h3 {
  font: var(--font-h3);
  margin-bottom: var(--space-3);
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

/* Charts */
.charts-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-3);
}
.chart-card canvas {
  max-height: 200px;
}

/* Sidebar */
.dash-sidebar { position: sticky; top: 76px; align-self: start; }

/* Alerts */
.alert-list { display: flex; flex-direction: column; gap: var(--space-2); }
.alert-item {
  padding: var(--space-2) 0;
  border-bottom: 1px solid var(--color-border);
}
.alert-item:last-child { border-bottom: none; }
.alert-badge {
  font: var(--font-caption);
  font-weight: 500;
  padding: 2px 6px;
  border-radius: 4px;
  display: inline-block;
  margin-bottom: 2px;
}
.alert-urgent { background: var(--color-danger-bg); color: var(--color-danger); }
.alert-info { background: var(--color-primary-light); color: var(--color-primary); }
.alert-item p { font: var(--font-caption); color: var(--color-text-secondary); }

/* Stats */
.stat-list { display: flex; flex-direction: column; }
.stat-row {
  display: flex;
  justify-content: space-between;
  padding: var(--space-2) 0;
  border-bottom: 1px solid var(--color-border);
  font: var(--font-small);
}
.stat-row:last-child { border-bottom: none; }
.stat-row span { color: var(--color-text-secondary); }
.stat-green { color: var(--color-success); }

/* Quick Actions */
.quick-actions { display: flex; flex-direction: column; gap: var(--space-2); }
.qa-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-3);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font: var(--font-small);
  font-weight: 500;
  color: var(--color-text-primary);
  text-decoration: none;
  transition: border-color 0.15s, background 0.15s;
}
.qa-btn:hover {
  border-color: var(--color-primary);
  background: var(--color-primary-light);
  color: var(--color-primary);
  text-decoration: none;
}

.panels-row { display: grid; grid-template-columns: 1fr 1fr; gap: var(--space-3); margin-bottom: var(--space-3); }
.avail-list { display: flex; flex-direction: column; }
.avail-row { display: flex; align-items: center; padding: 6px 0; border-bottom: 1px solid var(--color-border); font: var(--font-small); }
.avail-row:last-child { border-bottom: none; }
.avail-rank { flex: 1; font-weight: 500; }
.avail-bar-wrap { width: 100px; height: 6px; background: var(--color-border); border-radius: 3px; overflow: hidden; margin: 0 12px; }
.avail-bar { height: 100%; border-radius: 3px; background: var(--color-primary); }
.avail-count { font-weight: 600; color: var(--color-primary); min-width: 40px; text-align: right; }
.nat-list { display: flex; flex-direction: column; }
.nat-row2 { display: flex; align-items: center; padding: 6px 0; border-bottom: 1px solid var(--color-border); font: var(--font-small); }
.nat-row2:last-child { border-bottom: none; }
.nat-name2 { flex: 1; }
.nat-count2 { font-weight: 600; color: var(--color-primary); min-width: 50px; text-align: right; margin-right: 8px; }
.nat-pct2 { color: var(--color-text-tertiary); min-width: 36px; text-align: right; }

@media (max-width: 1024px) {
  .dash-layout { grid-template-columns: 1fr; }
  .charts-row { grid-template-columns: 1fr; }
}
@media (max-width: 768px) {
  .kpi-grid { grid-template-columns: repeat(2, 1fr); }
  .dash-header { flex-direction: column; gap: var(--space-3); align-items: flex-start; }
}
</style>




