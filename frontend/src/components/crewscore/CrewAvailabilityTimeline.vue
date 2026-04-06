<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const currentWindow = ref(0)
const expandedRank = ref(null)

const colors = ['#0A66C2','#E7A33E','#1D9E75','#534AB7','#D85A30','#888780','#D4537E','#639922','#BA7517','#A32D2D']

const windows = [
  { days: 10, total: 847, verified: 624, avgCrew: 79, officers: 312 },
  { days: 20, total: 1580, verified: 1164, avgCrew: 78, officers: 580 },
  { days: 30, total: 3412, verified: 2514, avgCrew: 78, officers: 1252 },
  { days: 40, total: 4890, verified: 3604, avgCrew: 77, officers: 1796 },
  { days: 50, total: 6215, verified: 4579, avgCrew: 77, officers: 2283 },
  { days: 60, total: 8103, verified: 5972, avgCrew: 76, officers: 2978 }
]

const baseRanks = [
  { name: 'Master', base: 42, crew: [
    { n:'Capt. Nikos Andreou', i:'NA', nat:'Greek', yrs:18, ai:94, cr:91 },
    { n:'Capt. James Okonkwo', i:'JO', nat:'Nigerian', yrs:15, ai:88, cr:85 }
  ]},
  { name: 'Chief Officer', base: 68, crew: [
    { n:'Simos Varias', i:'SV', nat:'Greek', yrs:12, ai:92, cr:87 },
    { n:'Andrei Pop', i:'AP', nat:'Romanian', yrs:9, ai:84, cr:80 }
  ]},
  { name: '2nd Officer', base: 95, crew: [
    { n:'Ruslan Goncharov', i:'RG', nat:'Ukrainian', yrs:6, ai:87, cr:82 },
    { n:'Carlo Reyes', i:'CR', nat:'Filipino', yrs:5, ai:79, cr:76 }
  ]},
  { name: '3rd Officer', base: 107, crew: [
    { n:'Juan Dela Cruz', i:'JD', nat:'Filipino', yrs:4, ai:92, cr:87 },
    { n:'Lazar Stoyanov', i:'LS', nat:'Bulgarian', yrs:2, ai:68, cr:64 }
  ]},
  { name: 'Chief Engineer', base: 38, crew: [
    { n:'Dmitry Petrov', i:'DP', nat:'Russian', yrs:12, ai:95, cr:91 },
    { n:'Raj Patel', i:'RP', nat:'Indian', yrs:14, ai:86, cr:83 }
  ]},
  { name: '2nd Engineer', base: 62, crew: [
    { n:'Miguel Santos', i:'MS', nat:'Filipino', yrs:7, ai:89, cr:84 },
    { n:'Viktor Koval', i:'VK', nat:'Ukrainian', yrs:8, ai:82, cr:78 }
  ]},
  { name: '3rd Engineer', base: 78, crew: [
    { n:'Paolo Castillo', i:'PC', nat:'Filipino', yrs:3, ai:76, cr:71 },
    { n:'Chen Wei', i:'CW', nat:'Chinese', yrs:4, ai:74, cr:69 }
  ]},
  { name: 'Bosun', base: 56, crew: [
    { n:'Mark Reyes', i:'MR', nat:'Filipino', yrs:8, ai:81, cr:85 }
  ]},
  { name: 'AB', base: 184, crew: [
    { n:'Radu Ionescu', i:'RI', nat:'Romanian', yrs:5, ai:74, cr:72 },
    { n:'Ahmad Hassan', i:'AH', nat:'Indonesian', yrs:3, ai:70, cr:68 }
  ]},
  { name: 'Oiler / Wiper', base: 117, crew: [
    { n:'Ivan Kolev', i:'IK', nat:'Bulgarian', yrs:4, ai:72, cr:70 }
  ]}
]

const baseNats = [
  { name: 'Filipino', base: 508, color: '#0A66C2' },
  { name: 'Ukrainian', base: 102, color: '#E7A33E' },
  { name: 'Indian', base: 76, color: '#1D9E75' },
  { name: 'Romanian', base: 51, color: '#534AB7' },
  { name: 'Russian', base: 42, color: '#D85A30' },
  { name: 'Greek', base: 34, color: '#888780' },
  { name: 'Other', base: 34, color: '#B4B2A9' }
]

const w = computed(() => windows[currentWindow.value])
const multiplier = computed(() => w.value.total / windows[0].total)

const ranks = computed(() => {
  const maxBase = Math.max(...baseRanks.map(r => r.base))
  return baseRanks.map((r, i) => ({
    ...r,
    count: Math.round(r.base * multiplier.value),
    pct: Math.round(r.base * multiplier.value / w.value.total * 100),
    barWidth: Math.round(r.base / maxBase * 100),
    color: colors[i]
  }))
})

const nats = computed(() => {
  const maxBase = Math.max(...baseNats.map(n => n.base))
  return baseNats.map(n => ({
    ...n,
    count: Math.round(n.base * multiplier.value),
    barWidth: Math.round(n.base / maxBase * 100)
  }))
})

function toggleRank(idx) {
  expandedRank.value = expandedRank.value === idx ? null : idx
}

function viewCandidate(name) {
  router.push('/seafarer/101')
}
</script>

<template>
  <div class="avail-widget">
    <!-- Timeline Tabs -->
    <div class="avail-tabs">
      <button
        v-for="(win, i) in windows"
        :key="i"
        class="avail-tab"
        :class="{ active: currentWindow === i }"
        @click="currentWindow = i"
      >
        <span class="tab-days">{{ win.days }}</span>
        <span class="tab-label">days</span>
        <span class="tab-count">{{ win.total.toLocaleString() }}</span>
      </button>
    </div>

    <!-- Summary Cards -->
    <div class="avail-summary">
      <div class="sum-card">
        <div class="sum-val sum-primary">{{ w.total.toLocaleString() }}</div>
        <div class="sum-label">Total available</div>
      </div>
      <div class="sum-card">
        <div class="sum-val sum-accent">{{ w.verified.toLocaleString() }}</div>
        <div class="sum-label">Verified profiles</div>
      </div>
      <div class="sum-card">
        <div class="sum-val sum-crew">{{ w.avgCrew }}</div>
        <div class="sum-label">Avg CrewScore</div>
      </div>
      <div class="sum-card">
        <div class="sum-val sum-primary">{{ w.officers.toLocaleString() }}</div>
        <div class="sum-label">Officers available</div>
      </div>
    </div>

    <!-- Rank Breakdown -->
    <h3 class="avail-section-title">Available by rank — click to see candidates</h3>
    <div class="rank-grid">
      <div
        v-for="(r, i) in ranks"
        :key="r.name"
        class="rank-card"
        :class="{ expanded: expandedRank === i }"
        @click="toggleRank(i)"
      >
        <div class="rank-top">
          <span class="rank-name">{{ r.name }}</span>
          <span class="rank-count" :style="{ color: r.color }">{{ r.count }}</span>
        </div>
        <div class="rank-bar">
          <div class="rank-fill" :style="{ width: r.barWidth + '%', background: r.color }"></div>
        </div>

        <!-- Expanded: show top candidates -->
        <div v-if="expandedRank === i" class="rank-detail" @click.stop>
          <div v-for="c in r.crew" :key="c.i" class="crew-row">
            <div class="crew-avatar" :style="{ background: r.color }">{{ c.i }}</div>
            <div class="crew-info">
              <div class="crew-name">{{ c.n }}</div>
              <div class="crew-meta">{{ c.nat }} · {{ c.yrs }} yrs</div>
            </div>
            <div class="crew-scores">
              <div class="crew-sc crew-ai">{{ c.ai }}</div>
              <div class="crew-sc crew-cr">{{ c.cr }}</div>
            </div>
            <button class="crew-btn" @click.stop="viewCandidate(c.n)">+ Shortlist</button>
          </div>
          <button class="view-all-btn" @click.stop="router.push('/search')">
            View all {{ r.count }} {{ r.name }}s
          </button>
        </div>
      </div>
    </div>

    <!-- Nationality Breakdown -->
    <h3 class="avail-section-title">Nationality breakdown</h3>
    <div class="nat-breakdown">
      <div v-for="n in nats" :key="n.name" class="nat-row">
        <span class="nat-name">{{ n.name }}</span>
        <div class="nat-bar-wrap">
          <div class="nat-bar" :style="{ width: n.barWidth + '%', background: n.color }"></div>
        </div>
        <span class="nat-count">{{ n.count.toLocaleString() }}</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.avail-widget { margin-bottom: var(--space-4); }

/* Timeline Tabs */
.avail-tabs {
  display: flex;
  border-radius: var(--radius-lg);
  overflow: hidden;
  border: 1px solid var(--color-border);
  margin-bottom: var(--space-4);
}
.avail-tab {
  flex: 1;
  padding: var(--space-3) var(--space-2);
  text-align: center;
  cursor: pointer;
  background: var(--color-white);
  border: none;
  border-right: 1px solid var(--color-border);
  transition: all 0.15s;
}
.avail-tab:last-child { border-right: none; }
.avail-tab:hover { background: var(--color-surface); }
.avail-tab.active { background: var(--color-primary); }
.tab-days {
  display: block;
  font-size: 18px;
  font-weight: 600;
  color: var(--color-text-primary);
}
.avail-tab.active .tab-days { color: white; }
.tab-label {
  display: block;
  font: var(--font-caption);
  color: var(--color-text-tertiary);
}
.avail-tab.active .tab-label { color: rgba(255,255,255,0.8); }
.tab-count {
  display: block;
  font-size: 20px;
  font-weight: 600;
  color: var(--color-primary);
  margin-top: 4px;
}
.avail-tab.active .tab-count { color: white; }

/* Summary */
.avail-summary {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-3);
  margin-bottom: var(--space-4);
}
.sum-card {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: var(--space-3);
  text-align: center;
}
.sum-val { font-size: 22px; font-weight: 600; }
.sum-primary { color: var(--color-primary); }
.sum-accent { color: var(--color-accent); }
.sum-crew { color: var(--color-agent); }
.sum-label { font: var(--font-caption); color: var(--color-text-secondary); margin-top: 2px; }

/* Section title */
.avail-section-title {
  font: var(--font-h3);
  margin-bottom: var(--space-3);
}

/* Rank Grid */
.rank-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-2);
  margin-bottom: var(--space-4);
}
.rank-card {
  background: var(--color-white);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  padding: var(--space-3);
  cursor: pointer;
  transition: border-color 0.15s, box-shadow 0.15s;
}
.rank-card:hover { border-color: var(--color-primary); }
.rank-card.expanded {
  border-color: var(--color-primary);
  background: var(--color-primary-light);
}
.rank-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.rank-name { font: var(--font-small); font-weight: 500; }
.rank-count { font-size: 18px; font-weight: 600; }
.rank-bar {
  height: 4px;
  background: var(--color-border);
  border-radius: 2px;
  margin-top: 6px;
  overflow: hidden;
}
.rank-fill {
  height: 100%;
  border-radius: 2px;
  transition: width 0.3s;
}

/* Rank Detail (expanded) */
.rank-detail {
  margin-top: var(--space-2);
  padding-top: var(--space-2);
  border-top: 1px solid var(--color-border);
}
.crew-row {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: 5px 0;
  border-bottom: 1px solid var(--color-border);
}
.crew-row:last-child { border-bottom: none; }
.crew-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  font-weight: 500;
  flex-shrink: 0;
}
.crew-info { flex: 1; min-width: 0; }
.crew-name { font: var(--font-caption); font-weight: 500; }
.crew-meta { font-size: 10px; color: var(--color-text-tertiary); }
.crew-scores { display: flex; gap: 4px; }
.crew-sc {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 9px;
  font-weight: 600;
}
.crew-ai { border: 1.5px solid var(--color-primary); color: var(--color-primary); }
.crew-cr { border: 1.5px solid var(--color-success); color: var(--color-success); }
.crew-btn {
  padding: 3px 8px;
  font-size: 10px;
  font-weight: 500;
  background: var(--color-primary);
  color: white;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  white-space: nowrap;
}
.crew-btn:hover { background: var(--color-primary-hover); }
.view-all-btn {
  display: block;
  width: 100%;
  margin-top: var(--space-2);
  padding: 6px;
  background: transparent;
  color: var(--color-primary);
  border: 1px solid var(--color-primary);
  border-radius: 12px;
  font: var(--font-caption);
  font-weight: 500;
  cursor: pointer;
  text-align: center;
}
.view-all-btn:hover { background: var(--color-white); }

/* Nationality */
.nat-breakdown { margin-bottom: var(--space-4); }
.nat-row {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: 4px 0;
  font: var(--font-small);
}
.nat-name { width: 80px; color: var(--color-text-secondary); font: var(--font-caption); }
.nat-bar-wrap {
  flex: 1;
  height: 8px;
  background: var(--color-border);
  border-radius: 4px;
  overflow: hidden;
}
.nat-bar { height: 100%; border-radius: 4px; transition: width 0.3s; }
.nat-count { min-width: 40px; text-align: right; font-weight: 500; font: var(--font-caption); }

@media (max-width: 768px) {
  .avail-tabs { flex-wrap: wrap; }
  .avail-tab { min-width: 33.33%; }
  .avail-summary { grid-template-columns: repeat(2, 1fr); }
  .rank-grid { grid-template-columns: 1fr; }
}
</style>
