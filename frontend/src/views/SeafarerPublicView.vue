<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import CrewScoreWidget from '@/components/crewscore/CrewScoreWidget.vue'
import ReviewForm from '@/components/crewscore/ReviewForm.vue'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()
const profileId = route.params.id
const showReviewModal = ref(false)

/* Demo data — in production this comes from API: GET /profiles/:id/public */
const profile = ref({
  fullName: "Simos Varias",
  currentRank: "Chief Officer",
  vesselTypeExperience: "Oil/Chemical Tanker",
  yearsOfExperience: 12,
  nationality: "Greek",
  location: "Athens, Greece",
  verifiedStatus: "VERIFIED",
  aiScore: 92,
  bio: "Experienced Chief Officer with 12 years in tanker operations. Deep expertise in oil/chemical cargo handling, ECDIS navigation, and ISM/ISPS compliance. Strong leadership skills with proven track record managing bridge teams across diverse multinational crews.",
  availableFrom: "May 2026",
  contractPreference: "6-9 months",
  desiredRank: "Master",
  preferredVesselTypes: ["Oil Tanker", "Chemical Tanker", "Product Tanker"],
  languages: ["Greek", "English", "Spanish"],
  profileCompleteness: 92,
  stats: { profileViews: 1284, shortlists: 47, inquiries: 8, connections: 312 },
  seaService: [
    { rank: "Chief Officer", vessel: "MT Aegean Warrior", vesselType: "Oil/Chemical Tanker", dwt: "75,000", flag: "Greece", company: "Tsakos Energy Navigation", startDate: "Jan 2025", endDate: "Feb 2026", duration: "14 months" },
    { rank: "2nd Officer", vessel: "MT Mediterranean Star", vesselType: "Oil Tanker", dwt: "115,000", flag: "Liberia", company: "Diana Shipping", startDate: "Mar 2023", endDate: "Dec 2024", duration: "22 months" },
    { rank: "3rd Officer", vessel: "MT Coral Bay", vesselType: "Chemical Tanker", dwt: "38,000", flag: "Malta", company: "Navios Group", startDate: "Jun 2021", endDate: "Jan 2023", duration: "19 months" },
    { rank: "3rd Officer", vessel: "MV Pacific Hope", vesselType: "Bulk Carrier", dwt: "82,000", flag: "Panama", company: "Capital Ship Management", startDate: "Aug 2019", endDate: "Apr 2021", duration: "20 months" }
  ],
  certificates: [
    { name: "COC - Chief Officer (Deck)", authority: "Hellenic Coast Guard", status: "VALID", expiry: "Dec 2030" },
    { name: "STCW Full Package", authority: "Manila Amendments", status: "VALID", expiry: "Aug 2028" },
    { name: "Advanced Tanker Cargo Operations", authority: "IMO/STCW", status: "VALID", expiry: "Nov 2027" },
    { name: "ECDIS Type-Specific", authority: "Transas/Furuno", status: "VALID", expiry: "Mar 2028" },
    { name: "Bridge Resource Management (BRM)", authority: "IMO", status: "VALID", expiry: "Aug 2027" },
    { name: "Ship Security Officer (SSO)", authority: "ISPS Code", status: "VALID", expiry: "Apr 2030" }
  ],
  flagEndorsements: ["Panama", "Marshall Islands", "Liberia", "Malta", "Bahamas"],
  skills: ["ECDIS", "GMDSS", "Cargo operations", "Bridge watch", "ISM/ISPS", "Tanker safety", "MARPOL", "ColRegs", "Passage planning"],
  education: { institution: "NTUA Athens", degree: "BSc Merchant Marine", years: "2007-2011" }
})

const shortlisted = ref(false)

function scoreClass(s) { return s >= 85 ? "ai-score-high" : s >= 70 ? "ai-score-mid" : "ai-score-low" }

function toggleShortlist() { shortlisted.value = !shortlisted.value }
</script>

<template>
  <div class="container pub-layout">
    <div class="pub-main">
      <!-- Hero Card -->
      <div class="card hero-card">
        <div class="hero-banner"></div>
        <div class="hero-content">
          <div class="hero-avatar avatar avatar-xl avatar-seafarer">{{ profile.fullName.split(' ').map(w => w[0]).join('').substring(0,2) }}</div>
          <div class="hero-info">
            <div class="hero-name-row">
              <h1>{{ profile.fullName }}</h1>
              <span v-if="profile.verifiedStatus === 'VERIFIED'" class="badge badge-verified">
                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 24 24" fill="currentColor"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/></svg>
                Verified
              </span>
            </div>
            <p class="hero-headline">{{ profile.currentRank }} · {{ profile.vesselTypeExperience }} · {{ profile.yearsOfExperience }} years experience</p>
            <p class="hero-location">{{ profile.nationality }} · {{ profile.location }}</p>
            <div class="hero-actions">
              <button class="btn" :class="shortlisted ? 'btn-shortlisted' : 'btn-primary'" @click="toggleShortlist">
                {{ shortlisted ? '✓ Shortlisted' : '+ Add to Shortlist' }}
              </button>
              <button class="btn btn-secondary" @click="router.push('/messages')">Message</button>
              <button class="btn btn-tertiary">Download CV</button>
            </div>
          </div>
        </div>
        <div class="hero-stats">
          <div class="stat"><strong>{{ profile.stats.profileViews.toLocaleString() }}</strong><span>Profile Views</span></div>
          <div class="stat"><strong>{{ profile.stats.shortlists }}</strong><span>Shortlists</span></div>
          <div class="stat"><strong>{{ profile.stats.inquiries }}</strong><span>Inquiries</span></div>
          <div class="stat"><strong>{{ profile.stats.connections }}</strong><span>Connections</span></div>
        </div>
      </div>

      <!-- About -->
      <div class="card section">
        <h2>About</h2>
        <p class="about-text">{{ profile.bio }}</p>
      </div>

      <!-- Sea Service -->
      <div class="card section">
        <h2>Sea Service Record</h2>
        <div class="service-list">
          <div v-for="(s, i) in profile.seaService" :key="i" class="service-item">
            <div class="service-dot" :class="i === 0 ? 'dot-current' : ''"></div>
            <div class="service-line" v-if="i < profile.seaService.length - 1"></div>
            <div class="service-info">
              <strong>{{ s.rank }} — {{ s.vessel }}</strong>
              <p class="service-detail">{{ s.vesselType }} · {{ s.dwt }} DWT · Flag: {{ s.flag }}</p>
              <p class="service-company">{{ s.company }}</p>
              <p class="service-dates">{{ s.startDate }} – {{ s.endDate }} · {{ s.duration }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Certificates -->
      <div class="card section">
        <h2>Certificates &amp; Licenses</h2>
        <div class="cert-list">
          <div v-for="c in profile.certificates" :key="c.name" class="cert-item">
            <div class="cert-info">
              <strong>{{ c.name }}</strong>
              <span class="cert-authority">{{ c.authority }}</span>
            </div>
            <div class="cert-right">
              <span class="badge" :class="c.status === 'VALID' ? 'badge-verified' : 'badge-warning'">{{ c.status }}</span>
              <span class="cert-expiry">Exp: {{ c.expiry }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Flag Endorsements -->
      <div class="card section">
        <h2>Flag State Endorsements</h2>
        <div class="tags-row">
          <span v-for="f in profile.flagEndorsements" :key="f" class="tag tag-flag">{{ f }}</span>
        </div>
      </div>

      <!-- Skills -->
      <div class="card section">
        <h2>Skills</h2>
        <div class="tags-row">
          <span v-for="s in profile.skills" :key="s" class="tag tag-skill">{{ s }}</span>
        </div>
      </div>

      <!-- CrewScore -->
      <CrewScoreWidget :showReviewButton="auth.isSeafarer" @requestReview="showReviewModal = true" />
    </div>

    <!-- Review Modal -->
    <div v-if="showReviewModal" class="modal-overlay" @click.self="showReviewModal = false">
      <div class="modal-content">
        <ReviewForm
          :seafarerName="profile.fullName"
          verifiedVessel="MT Aegean Warrior"
          overlapPeriod="Jan 2025 – Feb 2026"
          :overlapMonths="14"
          @submit="showReviewModal = false"
          @cancel="showReviewModal = false"
        />
      </div>
    </div>

    <!-- Sidebar -->
    <aside class="pub-sidebar">
      <!-- AI Score -->
      <div class="card section sidebar-score">
        <div class="ai-score-big" :class="scoreClass(profile.aiScore)">{{ profile.aiScore }}</div>
        <span class="score-label">AI Compatibility Score</span>
        <p class="score-sub">Based on your fleet requirements</p>
      </div>

      <!-- Availability -->
      <div class="card section">
        <h3>Availability</h3>
        <div class="avail-grid">
          <div class="avail-item"><span class="avail-label">Available from</span><strong>{{ profile.availableFrom }}</strong></div>
          <div class="avail-item"><span class="avail-label">Contract pref.</span><strong>{{ profile.contractPreference }}</strong></div>
          <div class="avail-item"><span class="avail-label">Desired rank</span><strong>{{ profile.desiredRank }}</strong></div>
          <div class="avail-item"><span class="avail-label">Vessel types</span><strong>{{ profile.preferredVesselTypes.join(', ') }}</strong></div>
        </div>
      </div>

      <!-- Languages -->
      <div class="card section">
        <h3>Languages</h3>
        <div class="tags-row">
          <span v-for="l in profile.languages" :key="l" class="tag tag-skill">{{ l }}</span>
        </div>
      </div>

      <!-- Education -->
      <div class="card section">
        <h3>Education</h3>
        <strong>{{ profile.education.institution }}</strong>
        <p class="edu-detail">{{ profile.education.degree }} · {{ profile.education.years }}</p>
      </div>

      <!-- Profile Completeness -->
      <div class="card section">
        <h3>Profile Completeness</h3>
        <div class="completeness-bar">
          <div class="completeness-fill" :style="{ width: profile.profileCompleteness + '%' }"></div>
        </div>
        <span class="completeness-text">{{ profile.profileCompleteness }}%</span>
      </div>
    </aside>
  </div>
</template>

<style scoped>
.pub-layout {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: var(--space-4);
  padding-top: var(--space-5);
  padding-bottom: var(--space-8);
}
.pub-main { min-width: 0; }

/* Hero */
.hero-card { margin-bottom: var(--space-3); }
.hero-banner { height: 120px; background: linear-gradient(135deg, var(--color-primary), var(--color-primary-dark)); }
.hero-content {
  display: flex;
  align-items: flex-end;
  gap: var(--space-4);
  padding: 0 var(--space-5);
  margin-top: -36px;
  position: relative;
  z-index: 2;
}
.hero-avatar { border: 3px solid var(--color-white); box-shadow: var(--shadow-md); }
.hero-info { padding-bottom: var(--space-4); padding-top: var(--space-2); }
.hero-name-row { display: flex; align-items: center; gap: var(--space-2); }
.hero-name-row h1 { font: var(--font-h1); }
.hero-headline { font: var(--font-body); color: var(--color-text-secondary); margin-top: 2px; }
.hero-location { font: var(--font-small); color: var(--color-text-tertiary); margin-top: 2px; }
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

.btn-shortlisted {
  background: var(--color-success-bg);
  color: var(--color-success);
  border: 1px solid var(--color-success);
}

/* Sections */
.section { padding: var(--space-4); margin-bottom: var(--space-3); }
.section h2 { font: var(--font-h2); margin-bottom: var(--space-3); }
.section h3 { font: var(--font-h3); margin-bottom: var(--space-3); }
.about-text { font: var(--font-body); color: var(--color-text-secondary); line-height: 1.7; }

/* Sea Service Timeline */
.service-list { position: relative; }
.service-item {
  display: flex;
  gap: var(--space-3);
  padding-bottom: var(--space-4);
  position: relative;
}
.service-item:last-child { padding-bottom: 0; }
.service-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: var(--color-primary);
  border: 2px solid var(--color-primary-light);
  flex-shrink: 0;
  margin-top: 4px;
  z-index: 1;
}
.dot-current { background: var(--color-success); border-color: var(--color-success-bg); }
.service-line {
  position: absolute;
  left: 5px;
  top: 20px;
  width: 2px;
  bottom: 0;
  background: var(--color-border);
}
.service-info { flex: 1; }
.service-info strong { font: var(--font-body); font-weight: 600; }
.service-detail { font: var(--font-small); color: var(--color-text-secondary); margin-top: 2px; }
.service-company { font: var(--font-small); color: var(--color-primary); margin-top: 1px; }
.service-dates { font: var(--font-caption); color: var(--color-text-tertiary); margin-top: 2px; }

/* Certificates */
.cert-list { display: flex; flex-direction: column; }
.cert-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-2) 0;
  border-bottom: 1px solid var(--color-border);
}
.cert-item:last-child { border-bottom: none; }
.cert-info strong { font: var(--font-small); font-weight: 500; display: block; }
.cert-authority { font: var(--font-caption); color: var(--color-text-tertiary); }
.cert-right { display: flex; align-items: center; gap: var(--space-2); }
.cert-expiry { font: var(--font-caption); color: var(--color-text-tertiary); }

/* Tags */
.tags-row { display: flex; flex-wrap: wrap; gap: var(--space-2); }
.tag {
  padding: 4px 10px;
  border-radius: 4px;
  font: var(--font-caption);
  font-weight: 500;
}
.tag-flag { background: var(--color-primary-light); color: var(--color-primary); }
.tag-skill { background: var(--color-surface); color: var(--color-text-secondary); }

/* Sidebar */
.pub-sidebar { position: sticky; top: 76px; align-self: start; }
.sidebar-score { text-align: center; }
.ai-score-big {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 700;
  margin: 0 auto var(--space-2);
}
.ai-score-high { border: 3px solid var(--color-success); color: var(--color-success); }
.ai-score-mid { border: 3px solid var(--color-primary); color: var(--color-primary); }
.ai-score-low { border: 3px solid var(--color-accent); color: var(--color-accent); }
.score-label { font: var(--font-small); font-weight: 600; color: var(--color-text-primary); display: block; }
.score-sub { font: var(--font-caption); color: var(--color-text-tertiary); }

.avail-grid { display: grid; grid-template-columns: 1fr 1fr; gap: var(--space-3); }
.avail-item { display: flex; flex-direction: column; gap: 2px; }
.avail-label { font: var(--font-caption); color: var(--color-text-tertiary); }
.avail-item strong { font: var(--font-small); }

.edu-detail { font: var(--font-small); color: var(--color-text-secondary); margin-top: 2px; }

.completeness-bar {
  height: 6px;
  background: var(--color-surface);
  border-radius: 3px;
  overflow: hidden;
  margin-bottom: var(--space-1);
}
.completeness-fill {
  height: 100%;
  background: var(--color-primary);
  border-radius: 3px;
}
.completeness-text { font: var(--font-caption); color: var(--color-text-secondary); }

/* Responsive */
@media (max-width: 768px) {
  .pub-layout { grid-template-columns: 1fr; }
  .hero-content { flex-direction: column; align-items: flex-start; padding: 0 var(--space-4); }
  .hero-stats { flex-wrap: wrap; gap: var(--space-3); }
  .hero-actions { flex-wrap: wrap; }
}

/* Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}
.modal-content {
  background: var(--color-white);
  border-radius: var(--radius-lg);
  max-width: 640px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
}
</style>
