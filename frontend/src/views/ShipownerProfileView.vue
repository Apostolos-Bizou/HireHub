<script setup>
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()

const company = ref({
  name: "Varship Shipping Co.",
  type: "Shipowner / Fleet Manager",
  location: "Piraeus, Greece",
  founded: "2005",
  website: "www.varship.gr",
  about: "Varship Shipping Co. is a leading international tanker company, providing seaborne transportation of crude oil, petroleum products, and LNG. With a modern fleet of 65+ vessels, we are committed to operational excellence, safety, and environmental responsibility.",
  stats: { vessels: 65, crew: 1560, nationalities: 12, deploymentsYTD: 487 },
  fleet: [
    { name: "MT Athens Glory", type: "Aframax Tanker", dwt: "115,000", flag: "Greece", built: 2019, crew: 24 },
    { name: "MT Parthenon", type: "Suezmax Tanker", dwt: "158,000", flag: "Liberia", built: 2021, crew: 25 },
    { name: "MV Aegean Star", type: "VLCC", dwt: "320,000", flag: "Marshall Islands", built: 2020, crew: 28 },
    { name: "MT Apollo Bay", type: "MR Product Tanker", dwt: "50,000", flag: "Malta", built: 2018, crew: 22 },
    { name: "MT Olympia", type: "LR2 Tanker", dwt: "115,000", flag: "Greece", built: 2022, crew: 24 },
    { name: "MT Delphi Sun", type: "Chemical Tanker", dwt: "37,000", flag: "Panama", built: 2017, crew: 21 }
  ],
  crewNeeds: [
    { rank: "3rd Officer", vesselType: "Oil/Chemical Tanker", positions: 3, urgency: "Urgent", deadline: "May 15, 2026" },
    { rank: "Chief Engineer", vesselType: "Bulk Carrier", positions: 1, urgency: "Standard", deadline: "Jun 1, 2026" },
    { rank: "2nd Officer", vesselType: "VLCC", positions: 2, urgency: "Standard", deadline: "Jul 2026" },
    { rank: "AB", vesselType: "Product Tanker", positions: 4, urgency: "Urgent", deadline: "May 20, 2026" }
  ],
  manningAgents: [
    { name: "Magsaysay Maritime Corp.", license: "DMW-1234-2026", location: "Manila", activeCrewFrom: 340 },
    { name: "CF Sharp Crew Management", license: "DMW-0892-2026", location: "Manila", activeCrewFrom: 185 },
    { name: "Bernhard Schulte SM", license: "DMW-0451-2026", location: "Manila", activeCrewFrom: 120 }
  ]
})

const activeTab = ref(0)
const tabs = ['Overview', 'Fleet', 'Crew Needs', 'Manning Agents']
</script>

<template>
  <div class="container profile-layout">
    <div class="profile-main">
      <!-- Hero -->
      <div class="card hero-card">
        <div class="hero-banner"></div>
        <div class="hero-content">
          <div class="hero-avatar avatar avatar-xl avatar-owner">TE</div>
          <div class="hero-info">
            <h1>{{ company.name }}</h1>
            <p class="hero-type">{{ company.type }}</p>
            <p class="hero-location">{{ company.location }} · Founded {{ company.founded }}</p>
            <div class="hero-actions">
              <button class="btn btn-primary">Edit Company Profile</button>
              <button class="btn btn-secondary">Share</button>
            </div>
          </div>
        </div>
        <div class="hero-stats">
          <div class="stat"><strong>{{ company.stats.vessels }}</strong><span>Vessels</span></div>
          <div class="stat"><strong>{{ company.stats.crew.toLocaleString() }}</strong><span>Active Crew</span></div>
          <div class="stat"><strong>{{ company.stats.nationalities }}</strong><span>Nationalities</span></div>
          <div class="stat"><strong>{{ company.stats.deploymentsYTD }}</strong><span>Deployments YTD</span></div>
        </div>
      </div>

      <!-- Tabs -->
      <div class="card tabs-card">
        <div class="tabs-row">
          <button v-for="(t, i) in tabs" :key="i" @click="activeTab = i" class="tab-btn" :class="{ active: activeTab === i }">{{ t }}</button>
        </div>
      </div>

      <!-- Overview Tab -->
      <div v-if="activeTab === 0">
        <div class="card section">
          <h2>About</h2>
          <p class="about-text">{{ company.about }}</p>
          <div class="about-details">
            <div class="detail-item">
              <span class="detail-label">Website</span>
              <a :href="'https://' + company.website" target="_blank">{{ company.website }}</a>
            </div>
            <div class="detail-item">
              <span class="detail-label">Headquarters</span>
              <span>{{ company.location }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Founded</span>
              <span>{{ company.founded }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Fleet size</span>
              <span>{{ company.stats.vessels }} vessels</span>
            </div>
          </div>
        </div>

        <div class="card section">
          <div class="section-header">
            <h2>Active Crew Needs</h2>
            <span class="badge badge-danger">{{ company.crewNeeds.filter(c => c.urgency === 'Urgent').length }} urgent</span>
          </div>
          <div class="needs-list">
            <div v-for="n in company.crewNeeds.slice(0, 3)" :key="n.rank" class="need-row">
              <div class="need-info">
                <strong>{{ n.rank }}</strong>
                <span>{{ n.vesselType }} · {{ n.positions }} position{{ n.positions > 1 ? 's' : '' }}</span>
              </div>
              <div class="need-right">
                <span class="badge" :class="n.urgency === 'Urgent' ? 'badge-danger' : 'badge-info'">{{ n.urgency }}</span>
                <span class="need-deadline">by {{ n.deadline }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Fleet Tab -->
      <div v-if="activeTab === 1">
        <div class="card section">
          <h2>Fleet — {{ company.fleet.length }} Vessels</h2>
          <div class="fleet-grid">
            <div v-for="v in company.fleet" :key="v.name" class="fleet-card">
              <div class="fleet-card-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M2 20h20"/><path d="M5 20V8l7-5 7 5v12"/></svg>
              </div>
              <h3>{{ v.name }}</h3>
              <p class="fleet-type">{{ v.type }}</p>
              <div class="fleet-details">
                <span>{{ v.dwt }} DWT</span>
                <span>Flag: {{ v.flag }}</span>
                <span>Built {{ v.built }}</span>
                <span>{{ v.crew }} crew</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Crew Needs Tab -->
      <div v-if="activeTab === 2">
        <div class="card section">
          <div class="section-header">
            <h2>Open Crew Positions</h2>
            <button class="btn btn-primary btn-sm">+ Add Position</button>
          </div>
          <div class="needs-list">
            <div v-for="n in company.crewNeeds" :key="n.rank + n.vesselType" class="need-row need-row-full">
              <div class="need-info">
                <strong>{{ n.rank }}</strong>
                <span>{{ n.vesselType }} · {{ n.positions }} position{{ n.positions > 1 ? 's' : '' }}</span>
              </div>
              <div class="need-right">
                <span class="badge" :class="n.urgency === 'Urgent' ? 'badge-danger' : 'badge-info'">{{ n.urgency }}</span>
                <span class="need-deadline">Deadline: {{ n.deadline }}</span>
                <button class="btn btn-secondary btn-sm">Search talent</button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Manning Agents Tab -->
      <div v-if="activeTab === 3">
        <div class="card section">
          <h2>Manning Agent Partners</h2>
          <div class="agents-list">
            <div v-for="a in company.manningAgents" :key="a.name" class="agent-row">
              <div class="avatar avatar-md avatar-agent">{{ a.name.split(' ').map(w => w[0]).join('').substring(0, 2) }}</div>
              <div class="agent-info">
                <strong>{{ a.name }}</strong>
                <span>{{ a.location }} · {{ a.activeCrewFrom }} active crew</span>
              </div>
              <span class="badge badge-agent">{{ a.license }}</span>
              <button class="btn btn-secondary btn-sm">Message</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Sidebar -->
    <aside class="profile-sidebar">
      <div class="card section">
        <h3>Quick Actions</h3>
        <div class="quick-actions">
          <button class="btn btn-primary" style="width:100%"  onclick="window.location.href='/search'">Search Talent</button>
          <button class="btn btn-secondary" style="width:100%" onclick="window.location.href='/shortlists'">View Shortlists</button>
          <button class="btn btn-tertiary" style="width:100%" onclick="window.location.href='/agent'">Agent Dashboard</button>
        </div>
      </div>
      <div class="card section">
        <h3>Fleet Summary</h3>
        <div class="fleet-summary">
          <div class="fs-item"><span>Tankers</span><strong>42</strong></div>
          <div class="fs-item"><span>Bulk Carriers</span><strong>12</strong></div>
          <div class="fs-item"><span>LNG/LPG</span><strong>8</strong></div>
          <div class="fs-item"><span>Container</span><strong>3</strong></div>
        </div>
      </div>
    </aside>
  </div>
</template>

<style scoped>
.profile-layout {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: var(--space-4);
  padding-top: var(--space-5);
  padding-bottom: var(--space-8);
}
.profile-main { min-width: 0; }

/* Hero */
.hero-card { margin-bottom: var(--space-3); }
.hero-banner { height: 120px; background: linear-gradient(135deg, var(--color-accent) 0%, #D4912F 100%); }
.hero-content {
  display: flex;
  align-items: flex-end;
  gap: var(--space-4);
  padding: 0 var(--space-5);
  margin-top: -36px;
  position: relative;
  z-index: 2;
}
.hero-avatar {
  border: 3px solid var(--color-white);
  box-shadow: var(--shadow-md);
}
.hero-info {
  padding-bottom: var(--space-4);
  padding-top: var(--space-2);
}
.hero-info h1 { font: var(--font-h1); }
.hero-type { font: var(--font-body); color: var(--color-accent); font-weight: 500; }
.hero-location { font: var(--font-small); color: var(--color-text-secondary); margin-top: 2px; }
.hero-actions { display: flex; gap: var(--space-2); margin-top: var(--space-3); }
.hero-stats {
  display: flex;
  gap: var(--space-5);
  padding: var(--space-3) var(--space-5);
  border-top: 1px solid var(--color-border);
}
.stat { text-align: center; }
.stat strong { display: block; font: var(--font-h2); color: var(--color-primary); }
.stat span { font: var(--font-caption); color: var(--color-text-secondary); }

/* Tabs */
.tabs-card { margin-bottom: var(--space-3); }
.tabs-row { display: flex; border-bottom: none; }
.tab-btn {
  flex: 1;
  padding: var(--space-3);
  background: none;
  border: none;
  font: var(--font-small);
  font-weight: 500;
  color: var(--color-text-secondary);
  cursor: pointer;
  border-bottom: 2px solid transparent;
}
.tab-btn:hover { background: var(--color-surface); }
.tab-btn.active { color: var(--color-primary); border-bottom-color: var(--color-primary); }

/* Sections */
.section { padding: var(--space-4); margin-bottom: var(--space-3); }
.section h2 { font: var(--font-h2); margin-bottom: var(--space-3); }
.section h3 { font: var(--font-h3); margin-bottom: var(--space-3); }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--space-3); }
.section-header h2 { margin-bottom: 0; }
.about-text { font: var(--font-body); color: var(--color-text-secondary); line-height: 1.7; margin-bottom: var(--space-4); }
.about-details { display: grid; grid-template-columns: 1fr 1fr; gap: var(--space-3); }
.detail-item { display: flex; flex-direction: column; gap: 2px; }
.detail-label { font: var(--font-caption); color: var(--color-text-tertiary); text-transform: uppercase; letter-spacing: 0.5px; }
.detail-item span, .detail-item a { font: var(--font-small); }

/* Crew Needs */
.needs-list { display: flex; flex-direction: column; }
.need-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-3) 0;
  border-bottom: 1px solid var(--color-border);
}
.need-row:last-child { border-bottom: none; }
.need-info strong { display: block; font: var(--font-body); font-weight: 500; }
.need-info span { font: var(--font-small); color: var(--color-text-secondary); }
.need-right { display: flex; align-items: center; gap: var(--space-2); }
.need-deadline { font: var(--font-caption); color: var(--color-text-tertiary); }

/* Fleet */
.fleet-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: var(--space-3); }
.fleet-card {
  padding: var(--space-4);
  background: var(--color-surface);
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border);
}
.fleet-card-icon { color: var(--color-accent); margin-bottom: var(--space-2); }
.fleet-card h3 { font: var(--font-body); font-weight: 600; }
.fleet-type { font: var(--font-small); color: var(--color-accent); font-weight: 500; margin-top: 2px; }
.fleet-details {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
  margin-top: var(--space-2);
  font: var(--font-caption);
  color: var(--color-text-secondary);
}
.fleet-details span { background: var(--color-white); padding: 2px 6px; border-radius: 3px; }

/* Agents */
.agents-list { display: flex; flex-direction: column; gap: var(--space-3); }
.agent-row {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3);
  background: var(--color-surface);
  border-radius: var(--radius-md);
}
.agent-info { flex: 1; }
.agent-info strong { display: block; font: var(--font-body); font-weight: 500; }
.agent-info span { font: var(--font-small); color: var(--color-text-secondary); }

/* Sidebar */
.profile-sidebar { position: sticky; top: 76px; align-self: start; }
.quick-actions { display: flex; flex-direction: column; gap: var(--space-2); }
.fleet-summary { display: flex; flex-direction: column; gap: var(--space-2); }
.fs-item {
  display: flex;
  justify-content: space-between;
  padding: var(--space-2) 0;
  border-bottom: 1px solid var(--color-border);
  font: var(--font-small);
}
.fs-item:last-child { border-bottom: none; }
.fs-item strong { color: var(--color-primary); }
.btn-sm { font-size: 12px; padding: 6px 14px; }

@media (max-width: 768px) {
  .profile-layout { grid-template-columns: 1fr; }
  .hero-content { flex-direction: column; align-items: flex-start; padding: 0 var(--space-4); }
  .hero-stats { flex-wrap: wrap; gap: var(--space-3); }
  .fleet-grid { grid-template-columns: 1fr; }
  .about-details { grid-template-columns: 1fr; }
}
</style>

