<script setup>
import { ref, onMounted } from "vue"
import { useRoute } from "vue-router"
import { useAuthStore } from "@/stores/auth"

const route = useRoute()
const auth = useAuthStore()
const profile = ref({
  fullName: "Juan Dela Cruz", currentRank: "3rd Officer", nationality: "Filipino",
  yearsOfExperience: 4, verifiedStatus: "VERIFIED", profileCompleteness: 92,
  bio: "Dedicated maritime professional with 4 years tanker experience. COC holder, STCW certified.",
  availableFrom: "2026-06-15", desiredRank: "2nd Officer", contractPreference: "6+2",
  profileViews: 347, shortlistCount: 12,
  seaServiceRecords: [
    { id: "1", vesselName: "MT Pacific Voyager", vesselType: "OIL_CHEMICAL_TANKER", dwt: "50,000", flagState: "Panama", rankHeld: "3rd Officer", companyName: "Aegean Maritime", startDate: "2024-03-01", endDate: "2025-01-15" },
    { id: "2", vesselName: "MT Golden Horizon", vesselType: "OIL_TANKER", dwt: "110,000", flagState: "Marshall Islands", rankHeld: "3rd Officer", companyName: "Diana Shipping", startDate: "2023-01-10", endDate: "2024-02-28" },
    { id: "3", vesselName: "MT Emerald Sea", vesselType: "CHEMICAL_TANKER", dwt: "25,000", flagState: "Liberia", rankHeld: "Cadet", companyName: "Capital Ship Mgmt", startDate: "2022-01-15", endDate: "2022-12-20" }
  ],
  certificates: [
    { id: "1", certName: "Certificate of Competency (COC)", certType: "COC", issuingAuthority: "MARINA", expiryDate: "2028-06-30", status: "VALID", verifiedStatus: "VERIFIED" },
    { id: "2", certName: "STCW Basic Safety", certType: "STCW", issuingAuthority: "MARINA", expiryDate: "2027-03-15", status: "VALID", verifiedStatus: "VERIFIED" },
    { id: "3", certName: "Tanker Familiarization", certType: "TANKER_FAMILIARIZATION", issuingAuthority: "STCW Center Manila", expiryDate: "2026-09-01", status: "EXPIRING", verifiedStatus: "VERIFIED" },
    { id: "4", certName: "Panama Flag Endorsement", certType: "FLAG_ENDORSEMENT", issuingAuthority: "Panama Maritime Authority", expiryDate: "2027-12-31", status: "VALID", verifiedStatus: "UNVERIFIED" }
  ],
  skills: ["ECDIS","GMDSS","Cargo operations","Bridge watch","ISM/ISPS","Tanker safety","MARPOL","English"],
  education: [{ institutionName: "Philippine Merchant Marine Academy", degree: "BS Marine Transportation", endYear: 2022 }]
})
</script>

<template>
  <div class="container profile-layout">
    <main class="profile-main">
      <!-- Hero Card -->
      <div class="card hero-card">
        <div class="hero-banner"></div>
        <div class="hero-body">
          <div class="avatar avatar-xl avatar-seafarer hero-avatar">{{ profile.fullName?.split(' ').map(w=>w[0]).join('').substring(0,2) }}</div>
          <div class="hero-info">
            <h1>{{ profile.fullName }} <span v-if="profile.verifiedStatus==='VERIFIED'" class="badge badge-verified">✓ Verified</span></h1>
            <p class="hero-headline">{{ profile.currentRank }} │ {{ profile.nationality }} │ {{ profile.yearsOfExperience }} years sea service</p>
            <div class="hero-stats">
              <span>👁 {{ profile.profileViews }} views</span>
              <span>📋 {{ profile.shortlistCount }} shortlists</span>
            </div>
          </div>
          <div class="hero-actions">
            <button class="btn btn-primary">+ Add to Shortlist</button>
            <button class="btn btn-secondary">💬 Message</button>
            <button class="btn btn-tertiary">📄 Download CV</button>
          </div>
        </div>
      </div>

      <!-- Sea Service -->
      <div class="card section">
        <h2>Sea Service Record</h2>
        <div v-for="ss in profile.seaServiceRecords" :key="ss.id" class="timeline-item">
          <div class="timeline-dot" :class="ss.rankHeld === 'Cadet' ? 'dot-cadet' : 'dot-officer'"></div>
          <div class="timeline-content">
            <strong>{{ ss.rankHeld }} — {{ ss.vesselName }}</strong>
            <p>{{ ss.vesselType?.replace(/_/g,' ') }} │ {{ ss.dwt }} DWT │ {{ ss.flagState }}</p>
            <p class="date">{{ ss.startDate }} – {{ ss.endDate || 'Present' }} │ {{ ss.companyName }}</p>
          </div>
        </div>
      </div>

      <!-- Certificates -->
      <div class="card section">
        <h2>Certificates & Licenses</h2>
        <div v-for="c in profile.certificates" :key="c.id" class="cert-row">
          <div class="cert-info">
            <strong>{{ c.certName }}</strong>
            <p>{{ c.issuingAuthority }} │ Expires: {{ c.expiryDate }}</p>
          </div>
          <div class="cert-badges">
            <span class="badge" :class="c.status==='VALID' ? 'badge-verified' : 'badge-warning'">{{ c.status }}</span>
            <span v-if="c.verifiedStatus==='VERIFIED'" class="badge badge-verified">✓ AI Verified</span>
          </div>
        </div>
      </div>

      <!-- Skills -->
      <div class="card section">
        <h2>Skills & Endorsements</h2>
        <div class="skills-grid">
          <span v-for="skill in profile.skills" :key="skill" class="badge badge-info">{{ skill }}</span>
        </div>
      </div>
    </main>

    <!-- Sidebar -->
    <aside class="profile-sidebar">
      <div class="card sidebar-section">
        <h3>AI Compatibility</h3>
        <div class="ai-score ai-score-high" style="width:64px;height:64px;font-size:20px;margin:var(--space-3) auto">92</div>
        <p style="text-align:center;font:var(--font-caption);color:var(--color-text-secondary)">Based on your search criteria</p>
      </div>
      <div class="card sidebar-section">
        <h3>Availability</h3>
        <div class="avail-grid">
          <div><label>Available from</label><strong>{{ profile.availableFrom }}</strong></div>
          <div><label>Contract pref.</label><strong>{{ profile.contractPreference }}</strong></div>
          <div><label>Desired rank</label><strong>{{ profile.desiredRank }}</strong></div>
        </div>
      </div>
      <div class="card sidebar-section">
        <h3>Education</h3>
        <div v-for="e in profile.education" :key="e.institutionName">
          <strong>{{ e.institutionName }}</strong>
          <p>{{ e.degree }} ({{ e.endYear }})</p>
        </div>
      </div>
      <div class="card sidebar-section">
        <h3>Profile Completeness</h3>
        <div class="progress-bar"><div class="progress-fill" :style="{width: profile.profileCompleteness + '%'}"></div></div>
        <p style="text-align:center;font:var(--font-caption);margin-top:var(--space-1)">{{ profile.profileCompleteness }}%</p>
      </div>
    </aside>
  </div>
</template>

<style scoped>
.profile-layout { display: grid; grid-template-columns: 1fr 300px; gap: var(--space-5); padding-top: var(--space-5); padding-bottom: var(--space-8); }
.hero-banner { height: 100px; background: linear-gradient(135deg, var(--color-primary), var(--color-primary-dark)); }
.hero-body { padding: var(--space-4) var(--space-5); display: flex; gap: var(--space-4); align-items: flex-start; flex-wrap: wrap; }
.hero-avatar { margin-top: -40px; border: 3px solid var(--color-white); }
.hero-info { flex: 1; min-width: 200px; }
.hero-info h1 { font: var(--font-h1); display: flex; align-items: center; gap: var(--space-2); flex-wrap: wrap; }
.hero-headline { font: var(--font-body); color: var(--color-text-secondary); margin-top: var(--space-1); }
.hero-stats { display: flex; gap: var(--space-4); margin-top: var(--space-2); font: var(--font-small); color: var(--color-text-secondary); }
.hero-actions { display: flex; gap: var(--space-2); flex-wrap: wrap; }
.section { padding: var(--space-5); margin-top: var(--space-3); }
.section h2 { font: var(--font-h2); margin-bottom: var(--space-4); }
.timeline-item { display: flex; gap: var(--space-3); margin-bottom: var(--space-4); position: relative; }
.timeline-dot { width: 12px; height: 12px; border-radius: var(--radius-full); margin-top: 4px; flex-shrink: 0; }
.dot-officer { background: var(--color-primary); }
.dot-cadet { background: var(--color-accent); }
.timeline-content strong { font: var(--font-body); font-weight: 500; }
.timeline-content p { font: var(--font-small); color: var(--color-text-secondary); }
.date { font: var(--font-caption); color: var(--color-text-tertiary); }
.cert-row { display: flex; justify-content: space-between; align-items: center; padding: var(--space-3) 0; border-bottom: 1px solid var(--color-border); }
.cert-row:last-child { border-bottom: none; }
.cert-info strong { font: var(--font-body); font-weight: 500; }
.cert-info p { font: var(--font-small); color: var(--color-text-secondary); }
.cert-badges { display: flex; gap: var(--space-2); }
.skills-grid { display: flex; flex-wrap: wrap; gap: var(--space-2); }
.sidebar-section { padding: var(--space-4); margin-bottom: var(--space-3); }
.sidebar-section h3 { font: var(--font-h3); margin-bottom: var(--space-3); }
.avail-grid { display: grid; grid-template-columns: 1fr 1fr; gap: var(--space-3); }
.avail-grid label { font: var(--font-caption); color: var(--color-text-tertiary); display: block; }
.avail-grid strong { font: var(--font-small); }
.progress-bar { height: 8px; background: var(--color-surface); border-radius: 4px; overflow: hidden; }
.progress-fill { height: 100%; background: var(--color-primary); border-radius: 4px; transition: width 0.3s; }
@media (max-width: 768px) { .profile-layout { grid-template-columns: 1fr; } }
</style>
