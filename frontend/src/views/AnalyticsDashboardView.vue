<script setup>
import { ref, computed } from "vue"

// KPI data matching GTM targets
const period = ref("month")
const kpis = ref({
  profiles: { current: 2847, target: 5000, prev: 1920 },
  owners: { current: 12, target: 50, prev: 7 },
  agents: { current: 6, target: 20, prev: 3 },
  mrr: { current: 4580, target: 15000, prev: 2100 },
  shortlists: { current: 87, target: 500, prev: 42 },
  forwarded: { current: 234, target: 2000, prev: 98 },
  dau_mau: { current: 18.5, target: 25, prev: 14.2 },
  nps: { current: 47, target: 50, prev: 38 }
})

const growth = (cur, prev) => prev > 0 ? Math.round(((cur - prev) / prev) * 100) : 0
const pct = (cur, target) => Math.min(100, Math.round((cur / target) * 100))

// Monthly trend data
const monthlyData = [
  { month: "Jan", profiles: 320, owners: 2, mrr: 600, shortlists: 5 },
  { month: "Feb", profiles: 680, owners: 3, mrr: 900, shortlists: 12 },
  { month: "Mar", profiles: 1240, owners: 5, mrr: 1500, shortlists: 22 },
  { month: "Apr", profiles: 1920, owners: 7, mrr: 2100, shortlists: 42 },
  { month: "May", profiles: 2847, owners: 12, mrr: 4580, shortlists: 87 },
]

const maxProfiles = computed(() => Math.max(...monthlyData.map(d => d.profiles)))
const maxMrr = computed(() => Math.max(...monthlyData.map(d => d.mrr)))

// Acquisition channels
const channels = [
  { name: "Maritime Academies", users: 890, pct: 31, cac: 0.42, color: "#1B5E20" },
  { name: "Facebook Groups", users: 724, pct: 25, cac: 1.35, color: "#0A66C2" },
  { name: "Referral Program", users: 512, pct: 18, cac: 1.80, color: "#E7A33E" },
  { name: "STCW Training Centers", users: 398, pct: 14, cac: 0.68, color: "#1D9E75" },
  { name: "SEO / Content", users: 241, pct: 9, cac: 0.95, color: "#004182" },
  { name: "Other", users: 82, pct: 3, cac: 2.50, color: "#999999" },
]

// Top talent pipeline
const pipeline = [
  { rank: "3rd Officer", demand: 34, supply: 892, ratio: "26:1", status: "balanced" },
  { rank: "Chief Engineer", demand: 18, supply: 156, ratio: "9:1", status: "tight" },
  { rank: "2nd Officer", demand: 22, supply: 634, ratio: "29:1", status: "balanced" },
  { rank: "Master", demand: 8, supply: 89, ratio: "11:1", status: "tight" },
  { rank: "AB", demand: 45, supply: 1203, ratio: "27:1", status: "surplus" },
]

// Geography
const geoData = [
  { country: "Philippines", profiles: 1876, pct: 66 },
  { country: "Ukraine", profiles: 412, pct: 14 },
  { country: "Romania", profiles: 234, pct: 8 },
  { country: "India", profiles: 189, pct: 7 },
  { country: "Other", profiles: 136, pct: 5 },
]
</script>

<template>
  <div class="container dashboard">
    <div class="dash-header">
      <div>
        <h1>Analytics Dashboard</h1>
        <p class="subtitle">HireHub Performance Metrics — May 2026</p>
      </div>
      <div class="period-toggle">
        <button :class="{ active: period === 'week' }" @click="period = 'week'">Week</button>
        <button :class="{ active: period === 'month' }" @click="period = 'month'">Month</button>
        <button :class="{ active: period === 'quarter' }" @click="period = 'quarter'">Quarter</button>
      </div>
    </div>

    <!-- KPI Cards Row 1 -->
    <div class="kpi-grid">
      <div class="card kpi-card">
        <div class="kpi-label">Verified Profiles</div>
        <div class="kpi-value">{{ kpis.profiles.current.toLocaleString() }}</div>
        <div class="kpi-meta">
          <span class="growth positive">↑ {{ growth(kpis.profiles.current, kpis.profiles.prev) }}%</span>
          <span class="target">Target: {{ kpis.profiles.target.toLocaleString() }}</span>
        </div>
        <div class="progress-bar"><div class="progress-fill" :style="{ width: pct(kpis.profiles.current, kpis.profiles.target) + '%' }"></div></div>
      </div>
      <div class="card kpi-card">
        <div class="kpi-label">Shipowner Accounts</div>
        <div class="kpi-value">{{ kpis.owners.current }}</div>
        <div class="kpi-meta">
          <span class="growth positive">↑ {{ growth(kpis.owners.current, kpis.owners.prev) }}%</span>
          <span class="target">Target: {{ kpis.owners.target }}</span>
        </div>
        <div class="progress-bar"><div class="progress-fill" :style="{ width: pct(kpis.owners.current, kpis.owners.target) + '%' }"></div></div>
      </div>
      <div class="card kpi-card">
        <div class="kpi-label">Monthly Revenue (MRR)</div>
        <div class="kpi-value">${{ kpis.mrr.current.toLocaleString() }}</div>
        <div class="kpi-meta">
          <span class="growth positive">↑ {{ growth(kpis.mrr.current, kpis.mrr.prev) }}%</span>
          <span class="target">Target: ${{ kpis.mrr.target.toLocaleString() }}</span>
        </div>
        <div class="progress-bar"><div class="progress-fill" :style="{ width: pct(kpis.mrr.current, kpis.mrr.target) + '%' }"></div></div>
      </div>
      <div class="card kpi-card">
        <div class="kpi-label">NPS Score</div>
        <div class="kpi-value">{{ kpis.nps.current }}</div>
        <div class="kpi-meta">
          <span class="growth positive">↑ {{ growth(kpis.nps.current, kpis.nps.prev) }}%</span>
          <span class="target">Target: {{ kpis.nps.target }}+</span>
        </div>
        <div class="progress-bar"><div class="progress-fill" :style="{ width: pct(kpis.nps.current, kpis.nps.target) + '%' }"></div></div>
      </div>
    </div>

    <!-- Row 2: Charts -->
    <div class="charts-row">
      <!-- Profile Growth Chart -->
      <div class="card chart-card">
        <h3>Profile Growth</h3>
        <div class="bar-chart">
          <div v-for="d in monthlyData" :key="d.month" class="bar-group">
            <div class="bar" :style="{ height: (d.profiles / maxProfiles * 160) + 'px' }">
              <span class="bar-value">{{ d.profiles.toLocaleString() }}</span>
            </div>
            <span class="bar-label">{{ d.month }}</span>
          </div>
        </div>
      </div>

      <!-- MRR Growth Chart -->
      <div class="card chart-card">
        <h3>MRR Growth ($)</h3>
        <div class="bar-chart">
          <div v-for="d in monthlyData" :key="d.month" class="bar-group">
            <div class="bar mrr-bar" :style="{ height: (d.mrr / maxMrr * 160) + 'px' }">
              <span class="bar-value">${{ d.mrr.toLocaleString() }}</span>
            </div>
            <span class="bar-label">{{ d.month }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Row 3: Acquisition + Geography -->
    <div class="charts-row">
      <!-- Acquisition Channels -->
      <div class="card chart-card">
        <h3>Acquisition Channels</h3>
        <div class="channel-list">
          <div v-for="ch in channels" :key="ch.name" class="channel-row">
            <div class="channel-info">
              <div class="channel-dot" :style="{ background: ch.color }"></div>
              <span class="channel-name">{{ ch.name }}</span>
            </div>
            <div class="channel-bar-wrap">
              <div class="channel-bar" :style="{ width: ch.pct + '%', background: ch.color }"></div>
            </div>
            <div class="channel-stats">
              <span>{{ ch.users }}</span>
              <span class="cac">${{ ch.cac }}</span>
            </div>
          </div>
        </div>
        <div class="channel-legend">
          <span>Users</span><span>CAC</span>
        </div>
      </div>

      <!-- Geography -->
      <div class="card chart-card">
        <h3>Profiles by Nationality</h3>
        <div class="geo-list">
          <div v-for="g in geoData" :key="g.country" class="geo-row">
            <span class="geo-country">{{ g.country }}</span>
            <div class="geo-bar-wrap">
              <div class="geo-bar" :style="{ width: g.pct + '%' }"></div>
            </div>
            <span class="geo-count">{{ g.profiles.toLocaleString() }} ({{ g.pct }}%)</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Row 4: Pipeline + Operational -->
    <div class="charts-row">
      <!-- Talent Pipeline -->
      <div class="card chart-card">
        <h3>Talent Supply vs Demand</h3>
        <table class="data-table">
          <thead>
            <tr><th>Rank</th><th>Open Positions</th><th>Available</th><th>Ratio</th><th>Status</th></tr>
          </thead>
          <tbody>
            <tr v-for="p in pipeline" :key="p.rank">
              <td><strong>{{ p.rank }}</strong></td>
              <td>{{ p.demand }}</td>
              <td>{{ p.supply }}</td>
              <td>{{ p.ratio }}</td>
              <td><span class="badge" :class="p.status === 'tight' ? 'badge-warning' : p.status === 'surplus' ? 'badge-verified' : 'badge-info'">{{ p.status }}</span></td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Operational Metrics -->
      <div class="card chart-card">
        <h3>Operational Metrics</h3>
        <div class="ops-grid">
          <div class="ops-item">
            <div class="ops-value">32h</div>
            <div class="ops-label">Avg. view → shortlist</div>
            <div class="ops-target">Target: &lt;48h ✓</div>
          </div>
          <div class="ops-item">
            <div class="ops-value">3.8d</div>
            <div class="ops-label">Avg. shortlist → agent</div>
            <div class="ops-target">Target: &lt;5d ✓</div>
          </div>
          <div class="ops-item">
            <div class="ops-value">84%</div>
            <div class="ops-label">Profile completeness</div>
            <div class="ops-target">Target: &gt;80% ✓</div>
          </div>
          <div class="ops-item">
            <div class="ops-value">96.2%</div>
            <div class="ops-label">Cert verification accuracy</div>
            <div class="ops-target">Target: &gt;95% ✓</div>
          </div>
          <div class="ops-item">
            <div class="ops-value">34%</div>
            <div class="ops-label">AI match → hired rate</div>
            <div class="ops-target">Target: &gt;30% ✓</div>
          </div>
          <div class="ops-item">
            <div class="ops-value">2.8%</div>
            <div class="ops-label">Owner monthly churn</div>
            <div class="ops-target">Target: &lt;5% ✓</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dashboard { padding-top: var(--space-5); padding-bottom: var(--space-8); }
.dash-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--space-5); }
.dash-header h1 { font: var(--font-h1); }
.subtitle { font: var(--font-small); color: var(--color-text-secondary); }
.period-toggle { display: flex; gap: 2px; background: var(--color-surface); border-radius: 20px; padding: 2px; }
.period-toggle button { padding: 6px 16px; border: none; background: none; font: var(--font-small); cursor: pointer; border-radius: 18px; color: var(--color-text-secondary); }
.period-toggle button.active { background: var(--color-primary); color: var(--color-white); }

.kpi-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: var(--space-3); margin-bottom: var(--space-4); }
.kpi-card { padding: var(--space-4); }
.kpi-label { font: var(--font-small); color: var(--color-text-secondary); margin-bottom: var(--space-1); }
.kpi-value { font-size: 28px; font-weight: 600; color: var(--color-text-primary); }
.kpi-meta { display: flex; justify-content: space-between; align-items: center; margin: var(--space-2) 0; }
.growth { font: var(--font-caption); font-weight: 500; }
.growth.positive { color: var(--color-success); }
.target { font: var(--font-caption); color: var(--color-text-tertiary); }
.progress-bar { height: 6px; background: var(--color-surface); border-radius: 3px; overflow: hidden; }
.progress-fill { height: 100%; background: var(--color-primary); border-radius: 3px; transition: width 0.5s; }

.charts-row { display: grid; grid-template-columns: 1fr 1fr; gap: var(--space-4); margin-bottom: var(--space-4); }
.chart-card { padding: var(--space-4); }
.chart-card h3 { font: var(--font-h3); margin-bottom: var(--space-4); }

.bar-chart { display: flex; align-items: flex-end; gap: var(--space-3); height: 200px; padding-top: var(--space-4); }
.bar-group { flex: 1; display: flex; flex-direction: column; align-items: center; }
.bar { background: var(--color-primary); border-radius: 4px 4px 0 0; min-height: 4px; width: 100%; max-width: 48px; position: relative; transition: height 0.5s; }
.mrr-bar { background: var(--color-success); }
.bar-value { position: absolute; top: -20px; left: 50%; transform: translateX(-50%); font: var(--font-caption); font-weight: 500; white-space: nowrap; }
.bar-label { font: var(--font-caption); color: var(--color-text-secondary); margin-top: var(--space-2); }

.channel-list { display: flex; flex-direction: column; gap: var(--space-3); }
.channel-row { display: grid; grid-template-columns: 140px 1fr 80px; gap: var(--space-2); align-items: center; }
.channel-info { display: flex; align-items: center; gap: var(--space-2); }
.channel-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
.channel-name { font: var(--font-small); }
.channel-bar-wrap { height: 8px; background: var(--color-surface); border-radius: 4px; overflow: hidden; }
.channel-bar { height: 100%; border-radius: 4px; transition: width 0.5s; }
.channel-stats { display: flex; gap: var(--space-2); font: var(--font-caption); }
.cac { color: var(--color-text-tertiary); }
.channel-legend { display: flex; justify-content: flex-end; gap: var(--space-4); font: var(--font-caption); color: var(--color-text-tertiary); margin-top: var(--space-2); }

.geo-list { display: flex; flex-direction: column; gap: var(--space-3); }
.geo-row { display: grid; grid-template-columns: 100px 1fr 100px; gap: var(--space-2); align-items: center; }
.geo-country { font: var(--font-small); font-weight: 500; }
.geo-bar-wrap { height: 10px; background: var(--color-surface); border-radius: 5px; overflow: hidden; }
.geo-bar { height: 100%; background: var(--color-primary); border-radius: 5px; }
.geo-count { font: var(--font-caption); color: var(--color-text-secondary); text-align: right; }

.data-table { width: 100%; border-collapse: collapse; }
.data-table th { font: var(--font-caption); font-weight: 500; color: var(--color-text-secondary); text-align: left; padding: var(--space-2); border-bottom: 1px solid var(--color-border); }
.data-table td { font: var(--font-small); padding: var(--space-2); border-bottom: 1px solid var(--color-border); }

.ops-grid { display: grid; grid-template-columns: 1fr 1fr; gap: var(--space-4); }
.ops-item { text-align: center; padding: var(--space-3); background: var(--color-surface); border-radius: var(--radius-md); }
.ops-value { font-size: 24px; font-weight: 600; color: var(--color-primary); }
.ops-label { font: var(--font-caption); color: var(--color-text-secondary); margin-top: var(--space-1); }
.ops-target { font: var(--font-caption); color: var(--color-success); margin-top: var(--space-1); }

@media (max-width: 1024px) { .kpi-grid { grid-template-columns: repeat(2, 1fr); } .charts-row { grid-template-columns: 1fr; } }
@media (max-width: 640px) { .kpi-grid { grid-template-columns: 1fr; } }
</style>
