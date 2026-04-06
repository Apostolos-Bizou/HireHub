<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

/* Demo data — in production: GET /companies/:id/public */
const company = ref({
  name: "Varship Shipping Co.",
  type: "Shipowner / Fleet Manager",
  location: "Piraeus, Greece",
  country: "Greece",
  founded: "2005",
  website: "www.varship.gr",
  about: "Varship Shipping Co. is a dynamic Greek shipping company specializing in tanker and bulk carrier operations. With a modern fleet and a commitment to excellence, safety, and sustainability, Varship provides reliable seaborne transportation services across global trade routes.",
  stats: { vessels: 18, crew: 430, nationalities: 8 },
  vesselTypes: ["Aframax Tanker", "Suezmax Tanker", "Bulk Carrier", "Product Tanker", "Chemical Tanker"],
  tradingRoutes: ["Mediterranean", "Black Sea", "North Europe", "Middle East Gulf", "West Africa"],
  verifiedStatus: "VERIFIED"
})
</script>

<template>
  <div class="container company-pub-layout">
    <div class="company-pub-main">
      <!-- Hero -->
      <div class="card hero-card">
        <div class="hero-banner"></div>
        <div class="hero-content">
          <div class="avatar avatar-xl avatar-owner">{{ company.name.split(' ').map(w => w[0]).join('').substring(0,2) }}</div>
          <div class="hero-info">
            <div class="hero-name-row">
              <h1>{{ company.name }}</h1>
              <span v-if="company.verifiedStatus === 'VERIFIED'" class="badge badge-verified">
                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 24 24" fill="currentColor"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/></svg>
                Verified Company
              </span>
            </div>
            <p class="hero-type">{{ company.type }}</p>
            <p class="hero-location">{{ company.location }} · Founded {{ company.founded }}</p>
            <div class="hero-actions">
              <button class="btn btn-secondary">Follow</button>
              <button class="btn btn-tertiary">Visit website</button>
            </div>
          </div>
        </div>
        <div class="hero-stats">
          <div class="stat"><strong>{{ company.stats.vessels }}</strong><span>Vessels</span></div>
          <div class="stat"><strong>{{ company.stats.crew }}</strong><span>Active Crew</span></div>
          <div class="stat"><strong>{{ company.stats.nationalities }}</strong><span>Nationalities</span></div>
        </div>
      </div>

      <!-- About -->
      <div class="card section">
        <h2>About</h2>
        <p class="about-text">{{ company.about }}</p>
        <div class="about-details">
          <div class="detail-item"><span class="detail-label">Headquarters</span><span>{{ company.location }}</span></div>
          <div class="detail-item"><span class="detail-label">Country</span><span>{{ company.country }}</span></div>
          <div class="detail-item"><span class="detail-label">Website</span><a :href="'https://' + company.website" target="_blank">{{ company.website }}</a></div>
          <div class="detail-item"><span class="detail-label">Founded</span><span>{{ company.founded }}</span></div>
        </div>
      </div>

      <!-- Fleet Types -->
      <div class="card section">
        <h2>Fleet Composition</h2>
        <div class="tags-row">
          <span v-for="vt in company.vesselTypes" :key="vt" class="tag tag-vessel">{{ vt }}</span>
        </div>
      </div>

      <!-- Trading Routes -->
      <div class="card section">
        <h2>Trading Routes</h2>
        <div class="tags-row">
          <span v-for="r in company.tradingRoutes" :key="r" class="tag tag-route">{{ r }}</span>
        </div>
      </div>
    </div>

    <!-- Sidebar -->
    <aside class="company-pub-sidebar">
      <div class="card section">
        <h3>Company at a Glance</h3>
        <div class="glance-list">
          <div class="glance-item"><span>Industry</span><strong>Maritime / Shipping</strong></div>
          <div class="glance-item"><span>Specialization</span><strong>Tanker & Bulk</strong></div>
          <div class="glance-item"><span>Fleet size</span><strong>{{ company.stats.vessels }} vessels</strong></div>
          <div class="glance-item"><span>Active crew</span><strong>{{ company.stats.crew }}+</strong></div>
          <div class="glance-item"><span>Nationalities</span><strong>{{ company.stats.nationalities }}+</strong></div>
        </div>
      </div>

      <div class="card section notice-card">
        <p class="notice-text">
          <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg>
          Employment is arranged exclusively through DMW-licensed manning agents. HireHub does not recruit or place seafarers.
        </p>
      </div>
    </aside>
  </div>
</template>

<style scoped>
.company-pub-layout {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: var(--space-4);
  padding-top: var(--space-5);
  padding-bottom: var(--space-8);
}
.company-pub-main { min-width: 0; }

.hero-card { margin-bottom: var(--space-3); }
.hero-banner { height: 120px; background: linear-gradient(135deg, var(--color-accent), #D4912F); }
.hero-content {
  display: flex;
  align-items: flex-end;
  gap: var(--space-4);
  padding: 0 var(--space-5);
  margin-top: -36px;
  position: relative;
  z-index: 2;
}
.avatar-xl { border: 3px solid var(--color-white); box-shadow: var(--shadow-md); }
.hero-info { padding-bottom: var(--space-4); padding-top: var(--space-2); }
.hero-name-row { display: flex; align-items: center; gap: var(--space-2); }
.hero-name-row h1 { font: var(--font-h1); }
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

.section { padding: var(--space-4); margin-bottom: var(--space-3); }
.section h2 { font: var(--font-h2); margin-bottom: var(--space-3); }
.section h3 { font: var(--font-h3); margin-bottom: var(--space-3); }
.about-text { font: var(--font-body); color: var(--color-text-secondary); line-height: 1.7; margin-bottom: var(--space-4); }
.about-details { display: grid; grid-template-columns: 1fr 1fr; gap: var(--space-3); }
.detail-item { display: flex; flex-direction: column; gap: 2px; }
.detail-label { font: var(--font-caption); color: var(--color-text-tertiary); text-transform: uppercase; letter-spacing: 0.5px; }

.tags-row { display: flex; flex-wrap: wrap; gap: var(--space-2); }
.tag { padding: 4px 10px; border-radius: 4px; font: var(--font-caption); font-weight: 500; }
.tag-vessel { background: var(--color-accent-light); color: #8B6914; }
.tag-route { background: var(--color-primary-light); color: var(--color-primary); }

.company-pub-sidebar { position: sticky; top: 76px; align-self: start; }
.glance-list { display: flex; flex-direction: column; }
.glance-item {
  display: flex;
  justify-content: space-between;
  padding: var(--space-2) 0;
  border-bottom: 1px solid var(--color-border);
  font: var(--font-small);
}
.glance-item:last-child { border-bottom: none; }
.glance-item span { color: var(--color-text-secondary); }
.glance-item strong { color: var(--color-text-primary); }

.notice-card { background: var(--color-primary-light); border: none; }
.notice-text {
  display: flex;
  align-items: flex-start;
  gap: var(--space-2);
  font: var(--font-small);
  color: var(--color-primary-dark);
  line-height: 1.5;
}
.notice-text svg { flex-shrink: 0; margin-top: 2px; }

@media (max-width: 768px) {
  .company-pub-layout { grid-template-columns: 1fr; }
  .hero-content { flex-direction: column; align-items: flex-start; padding: 0 var(--space-4); }
}
</style>
