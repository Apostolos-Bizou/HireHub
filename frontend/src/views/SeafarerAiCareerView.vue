<script setup>
import { ref, computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
const auth = useAuthStore()
const activeTab = ref(0)

const tabs = [
  { name: 'AI Job match', tip: 'Εύρεση θέσεων που ταιριάζουν στο προφίλ σου — βαθμός, εμπειρία, πιστοποιητικά' },
  { name: 'Company match', tip: 'AI σύσταση εταιρειών βάσει του CV σου — μισθός, αξιολογήσεις, τύπος πλοίου' },
  { name: 'Salary advisor', tip: 'Τι μισθό μπορείς να ζητήσεις βάσει εμπειρίας, βαθμού, εθνικότητας' },
  { name: 'Career roadmap', tip: 'Βήματα για την επόμενη προαγωγή — πιστοποιητικά, εμπειρία, χρονοδιάγραμμα' },
  { name: 'Cert gap analysis', tip: 'Ποια πιστοποιητικά σου λείπουν για καλύτερες θέσεις' }
]

// ── Profile context (simulated for demo user) ──
const myProfile = {
  name: 'Simos Varias',
  rank: '3rd Officer',
  nextRank: '2nd Officer',
  vesselType: 'Oil/Chemical Tanker',
  experience: 4,
  nationality: 'Filipino',
  certs: ['COC III/1', 'STCW Basic Safety', 'Tanker Familiarization', 'ECDIS', 'BRM', 'GMDSS'],
  missingCerts: ['Advanced Firefighting (renewal)', 'Medical First Aid (renewal)', 'Proficiency in Survival Craft'],
  crewScore: 82,
  aiScore: 92
}

// ── Tab 0: AI Job Match ──
const jobMatches = [
  { id: 1, title: '3rd Officer — Oil/Chemical Tanker', company: 'Tsakos Energy Navigation', flag: 'Greece', vessels: 78, salary: '$3,400–$4,200/mo', matchScore: 96, matchReasons: ['Exact rank match', 'Tanker experience 4 yrs', 'All certs valid', 'Filipino nationality preferred'], urgent: true, posted: '2 days ago' },
  { id: 2, title: '3rd Officer — Product Tanker', company: 'Capital Ship Management', flag: 'Greece', vessels: 92, salary: '$3,200–$3,900/mo', matchScore: 91, matchReasons: ['Rank match', 'Tanker experience', 'STCW valid', 'High CrewScore 82'], urgent: false, posted: '4 days ago' },
  { id: 3, title: '3rd Officer — Chemical Tanker', company: 'Stolt-Nielsen', flag: 'Norway', vessels: 40, salary: '$3,800–$4,600/mo', matchScore: 88, matchReasons: ['Chemical tanker specialist', 'ECDIS certified', 'BRM completed'], urgent: true, posted: '1 day ago' },
  { id: 4, title: '3rd Officer — Crude Oil Tanker', company: 'Dorian LPG', flag: 'Greece', vessels: 22, salary: '$3,500–$4,000/mo', matchScore: 84, matchReasons: ['Tanker background', 'Good AI score 92', 'Available soon'], urgent: false, posted: '5 days ago' },
  { id: 5, title: '2nd Officer — Oil Tanker', company: 'Diana Shipping', flag: 'Greece', vessels: 36, salary: '$4,500–$5,800/mo', matchScore: 72, matchReasons: ['Promotion opportunity', '4 yrs experience qualifies', 'Need 2nd Officer COC upgrade'], urgent: false, posted: '1 week ago' }
]

// ── Tab 1: Company Match ──
const companyMatches = [
  { name: 'Tsakos Energy Navigation', country: 'Greece', vessels: 78, type: 'Oil/Chemical Tanker', matchScore: 95, avgSalary: '$3,800', crewRating: 4.2, retention: '87%', pros: ['Top pay for Filipino officers', 'Modern fleet (avg age 8 yrs)', 'Strong safety record', 'Manila manning agent'], cons: ['Long contracts (9 months)', 'Competitive selection'], initials: 'TE', color: '#0A66C2' },
  { name: 'Capital Ship Management', country: 'Greece', vessels: 92, type: 'Tanker & Bulk', matchScore: 90, avgSalary: '$3,500', crewRating: 4.0, retention: '82%', pros: ['Large fleet — many opportunities', 'Career progression path', 'Good CBA terms'], cons: ['Mixed fleet requires flexibility', 'Average facilities on older vessels'], initials: 'CS', color: '#1D9E75' },
  { name: 'Stolt-Nielsen', country: 'Norway', vessels: 40, type: 'Chemical Tanker', matchScore: 88, avgSalary: '$4,200', crewRating: 4.5, retention: '91%', pros: ['Highest pay in segment', 'Excellent crew welfare', 'Advanced training provided', 'Scandinavian standards'], cons: ['Strict performance requirements', 'Smaller fleet = fewer slots'], initials: 'SN', color: '#534AB7' },
  { name: 'Costamare', country: 'Greece', vessels: 114, type: 'Container', matchScore: 68, avgSalary: '$3,200', crewRating: 3.8, retention: '79%', pros: ['Huge fleet', 'Regular promotions', 'Manila-based management'], cons: ['Container experience needed', 'Lower pay than tanker segment'], initials: 'CM', color: '#E7A33E' }
]

// ── Tab 2: Salary Advisor ──
const salaryAdvice = {
  currentEstimate: '$3,400',
  marketLow: '$2,200',
  marketMedian: '$3,400',
  marketHigh: '$5,200',
  yourPosition: 'At median',
  negotiationTips: [
    { tip: 'Highlight your 4 years tanker experience — this is above average for 3rd Officers', impact: '+$200–400/mo' },
    { tip: 'Your CrewScore of 82 is top 25% — mention peer review ratings', impact: '+$100–300/mo' },
    { tip: 'ECDIS + BRM certifications are in high demand — negotiate on qualification premium', impact: '+$150–250/mo' },
    { tip: 'Target LPG/LNG companies — they pay 25% above tanker rates for qualified officers', impact: '+$500–800/mo' },
    { tip: 'Consider Greek-flag companies — they pay premium rates for experienced Filipino officers', impact: '+$300–500/mo' }
  ],
  potentialSalary: '$4,000–$4,800',
  potentialIncrease: '+18–41%'
}

// ── Tab 3: Career Roadmap ──
const roadmap = {
  currentRank: '3rd Officer',
  nextRank: '2nd Officer',
  timeToPromotion: '12–18 months',
  requirements: [
    { item: 'Sea service: 12 months as 3rd Officer', status: 'done', detail: 'You have 4 years — requirement exceeded' },
    { item: 'COC upgrade to II/1 (2nd Officer)', status: 'action', detail: 'Schedule exam with MARINA — est. 2-3 months preparation' },
    { item: 'Advanced Firefighting refresher', status: 'action', detail: 'Certificate expiring — renew before next deployment' },
    { item: 'Medical First Aid refresher', status: 'action', detail: 'Renewal needed within 60 days' },
    { item: 'Company assessment / interview', status: 'pending', detail: 'Most companies require bridge simulator assessment' },
    { item: 'CrewScore above 75', status: 'done', detail: 'Your CrewScore is 82 — well above threshold' }
  ],
  futureRanks: [
    { rank: '2nd Officer', timeline: '2027–2029', salary: '$4,500–$6,000', requirements: 'COC II/1 + 12 months as 3rd Officer' },
    { rank: 'Chief Officer', timeline: '2029–2032', salary: '$6,500–$9,500', requirements: 'COC II/2 + 18 months as 2nd Officer + Advanced shiphandling' },
    { rank: 'Master', timeline: '2032+', salary: '$8,500–$18,000', requirements: 'COC II/1 Master + 36 months as Chief Officer + Company approval' }
  ]
}

// ── Tab 4: Certificate Gap Analysis ──
const certAnalysis = {
  valid: [
    { name: 'COC III/1 (3rd Officer)', expiry: 'Dec 2028', status: 'valid', priority: 'ok' },
    { name: 'STCW Basic Safety', expiry: 'Mar 2027', status: 'valid', priority: 'ok' },
    { name: 'Tanker Familiarization', expiry: 'Jun 2028', status: 'valid', priority: 'ok' },
    { name: 'ECDIS Generic', expiry: 'No expiry', status: 'valid', priority: 'ok' },
    { name: 'BRM (Bridge Resource Management)', expiry: 'Sep 2027', status: 'valid', priority: 'ok' },
    { name: 'GMDSS (GOC)', expiry: 'Nov 2027', status: 'valid', priority: 'ok' }
  ],
  expiring: [
    { name: 'Advanced Firefighting', expiry: 'May 2026', status: 'expiring', priority: 'urgent', cost: '$280', duration: '3 days' },
    { name: 'Medical First Aid', expiry: 'Jun 2026', status: 'expiring', priority: 'warning', cost: '$200', duration: '2 days' }
  ],
  missing: [
    { name: 'Proficiency in Survival Craft (PSC)', status: 'missing', priority: 'recommended', cost: '$350', duration: '4 days', benefit: 'Required by 60% of tanker companies' },
    { name: 'Advanced Oil Tanker Operations', status: 'missing', priority: 'recommended', cost: '$450', duration: '5 days', benefit: 'Opens Chemical/Oil tanker senior positions' },
    { name: 'COC II/1 (2nd Officer)', status: 'missing', priority: 'career', cost: '$800', duration: '3 months prep + exam', benefit: 'Required for promotion to 2nd Officer — salary jump +35%' }
  ],
  totalInvestment: '$2,080',
  salaryImpact: '+$1,200–1,800/mo after promotion'
}

function scoreColor(s) {
  if (s >= 90) return '#1B5E20'
  if (s >= 80) return '#0A66C2'
  if (s >= 70) return '#E7A33E'
  return '#B71C1C'
}
</script>

<template>
  <div class="container ai-career-page">
    <!-- Header -->
    <div class="career-header">
      <div>
        <h1>
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M12 2a4 4 0 0 1 4 4c0 1.95-1.4 3.58-3.25 3.93L12 22"/><path d="M12 2a4 4 0 0 0-4 4c0 1.95 1.4 3.58 3.25 3.93"/><circle cx="12" cy="6" r="1.5"/></svg>
          AI Career Advisor
        </h1>
        <p class="career-sub">Personalized recommendations based on your profile · {{ myProfile.rank }} · {{ myProfile.experience }} yrs experience</p>
      </div>
      <div class="profile-context">
        <div class="pc-score" :style="{ borderColor: scoreColor(myProfile.aiScore) }">{{ myProfile.aiScore }}</div>
        <div class="pc-info">
          <span class="pc-rank">{{ myProfile.rank }}</span>
          <span class="pc-detail">CrewScore {{ myProfile.crewScore }} · {{ myProfile.vesselType }}</span>
        </div>
      </div>
    </div>

    <!-- Tabs -->
    <div class="career-tabs">
      <button v-for="(t, i) in tabs" :key="i" class="career-tab" :class="{ active: activeTab === i }" @click="activeTab = i" :title="t.tip">
        {{ t.name }}
      </button>
    </div>

    <!-- Tab 0: AI Job Match -->
    <div v-if="activeTab === 0">
      <div class="ai-banner">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2a4 4 0 0 1 4 4c0 1.95-1.4 3.58-3.25 3.93L12 22"/><path d="M12 2a4 4 0 0 0-4 4c0 1.95 1.4 3.58 3.25 3.93"/><circle cx="12" cy="6" r="1.5"/></svg>
        AI has analyzed {{ jobMatches.length }} open positions matching your profile. Ranked by compatibility score.
      </div>
      <div class="job-list">
        <div v-for="j in jobMatches" :key="j.id" class="card job-card">
          <div class="job-top">
            <div class="job-info">
              <h3>{{ j.title }} <span v-if="j.urgent" class="badge badge-urgent">Urgent</span></h3>
              <p class="job-meta">{{ j.company }} · {{ j.flag }} · {{ j.vessels }} vessels · {{ j.posted }}</p>
            </div>
            <div class="job-score" :style="{ borderColor: scoreColor(j.matchScore), color: scoreColor(j.matchScore) }">{{ j.matchScore }}%</div>
          </div>
          <div class="job-salary">{{ j.salary }}</div>
          <div class="job-reasons">
            <span v-for="r in j.matchReasons" :key="r" class="reason-tag">{{ r }}</span>
          </div>
          <div class="job-actions">
            <button class="btn btn-primary btn-sm">Express interest</button>
            <button class="btn btn-secondary btn-sm">View company</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Tab 1: Company Match -->
    <div v-if="activeTab === 1">
      <div class="ai-banner">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M2 20h20"/><path d="M5 20V8l7-5 7 5v12"/></svg>
        AI recommends these companies based on your rank, vessel type, certifications, and CrewScore.
      </div>
      <div class="company-match-list">
        <div v-for="c in companyMatches" :key="c.name" class="card company-match-card">
          <div class="cm-top">
            <div class="cm-avatar" :style="{ background: c.color }">{{ c.initials }}</div>
            <div class="cm-info">
              <h3>{{ c.name }}</h3>
              <p class="cm-meta">{{ c.country }} · {{ c.vessels }} vessels · {{ c.type }}</p>
            </div>
            <div class="cm-score" :style="{ borderColor: scoreColor(c.matchScore), color: scoreColor(c.matchScore) }">{{ c.matchScore }}%</div>
          </div>
          <div class="cm-stats">
            <div class="cm-stat"><span class="cm-stat-val">{{ c.avgSalary }}</span><span class="cm-stat-label">Avg salary</span></div>
            <div class="cm-stat"><span class="cm-stat-val">{{ c.crewRating }}/5</span><span class="cm-stat-label">Crew rating</span></div>
            <div class="cm-stat"><span class="cm-stat-val">{{ c.retention }}</span><span class="cm-stat-label">Retention</span></div>
          </div>
          <div class="cm-pros-cons">
            <div class="cm-pros">
              <div v-for="p in c.pros" :key="p" class="cm-item cm-pro"><span class="cm-dot cm-dot-g"></span>{{ p }}</div>
            </div>
            <div class="cm-cons">
              <div v-for="con in c.cons" :key="con" class="cm-item cm-con"><span class="cm-dot cm-dot-r"></span>{{ con }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Tab 2: Salary Advisor -->
    <div v-if="activeTab === 2">
      <div class="card section">
        <h2>Your salary position — {{ myProfile.rank }}</h2>
        <div class="sa-kpis">
          <div class="sa-kpi"><span class="sa-kpi-label">Market low</span><span class="sa-kpi-val">{{ salaryAdvice.marketLow }}</span></div>
          <div class="sa-kpi sa-kpi-highlight"><span class="sa-kpi-label">Your estimate</span><span class="sa-kpi-val sa-kpi-blue">{{ salaryAdvice.currentEstimate }}</span><span class="sa-kpi-sub">{{ salaryAdvice.yourPosition }}</span></div>
          <div class="sa-kpi"><span class="sa-kpi-label">Market high</span><span class="sa-kpi-val">{{ salaryAdvice.marketHigh }}</span></div>
        </div>
      </div>
      <div class="card section">
        <h2>
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg>
          AI negotiation tips
        </h2>
        <p class="section-sub">Personalized advice to maximize your salary</p>
        <div class="tips-list">
          <div v-for="(t, i) in salaryAdvice.negotiationTips" :key="i" class="tip-card">
            <div class="tip-num">{{ i + 1 }}</div>
            <div class="tip-body">
              <p class="tip-text">{{ t.tip }}</p>
              <span class="tip-impact">Potential impact: <strong>{{ t.impact }}</strong></span>
            </div>
          </div>
        </div>
      </div>
      <div class="card section sa-potential">
        <div class="sa-pot-label">With these strategies, your potential salary range:</div>
        <div class="sa-pot-val">{{ salaryAdvice.potentialSalary }}/mo</div>
        <div class="sa-pot-increase">{{ salaryAdvice.potentialIncrease }} vs current</div>
        <router-link to="/salary" class="btn btn-secondary" style="margin-top:var(--space-3)">Full salary benchmark</router-link>
      </div>
    </div>

    <!-- Tab 3: Career Roadmap -->
    <div v-if="activeTab === 3">
      <div class="card section">
        <h2>Your path: {{ roadmap.currentRank }} → {{ roadmap.nextRank }}</h2>
        <p class="section-sub">Estimated time to promotion: <strong>{{ roadmap.timeToPromotion }}</strong></p>
        <div class="roadmap-checklist">
          <div v-for="(r, i) in roadmap.requirements" :key="i" class="rm-item" :class="'rm-' + r.status">
            <div class="rm-icon">
              <span v-if="r.status === 'done'" class="rm-check">✓</span>
              <span v-else-if="r.status === 'action'" class="rm-action">!</span>
              <span v-else class="rm-pending">○</span>
            </div>
            <div class="rm-body">
              <strong>{{ r.item }}</strong>
              <p>{{ r.detail }}</p>
            </div>
          </div>
        </div>
      </div>
      <div class="card section">
        <h2>Long-term career projection</h2>
        <div class="career-timeline">
          <div v-for="f in roadmap.futureRanks" :key="f.rank" class="ct-item">
            <div class="ct-dot"></div>
            <div class="ct-body">
              <h3>{{ f.rank }}</h3>
              <div class="ct-meta">{{ f.timeline }} · {{ f.salary }}</div>
              <p class="ct-req">{{ f.requirements }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Tab 4: Certificate Gap Analysis -->
    <div v-if="activeTab === 4">
      <div class="card section">
        <h2>
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#B71C1C" stroke-width="1.8"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg>
          Certificates needing attention
        </h2>
        <div class="cert-list">
          <div v-for="c in certAnalysis.expiring" :key="c.name" class="cert-item cert-expiring">
            <div class="cert-status"><span class="cert-badge" :class="'cb-' + c.priority">{{ c.priority }}</span></div>
            <div class="cert-info">
              <strong>{{ c.name }}</strong>
              <span>Expires: {{ c.expiry }} · Renewal: {{ c.cost }} · {{ c.duration }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="card section">
        <h2>
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
          Recommended certifications
        </h2>
        <p class="section-sub">Certificates that will increase your employability and salary</p>
        <div class="cert-list">
          <div v-for="c in certAnalysis.missing" :key="c.name" class="cert-item cert-missing">
            <div class="cert-status"><span class="cert-badge" :class="'cb-' + c.priority">{{ c.priority }}</span></div>
            <div class="cert-info">
              <strong>{{ c.name }}</strong>
              <span>Cost: {{ c.cost }} · {{ c.duration }}</span>
              <p class="cert-benefit">{{ c.benefit }}</p>
            </div>
          </div>
        </div>
      </div>
      <div class="card section">
        <h2>Valid certificates ({{ certAnalysis.valid.length }})</h2>
        <div class="cert-list">
          <div v-for="c in certAnalysis.valid" :key="c.name" class="cert-item cert-valid">
            <div class="cert-status"><span class="cert-badge cb-ok">valid</span></div>
            <div class="cert-info">
              <strong>{{ c.name }}</strong>
              <span>Expires: {{ c.expiry }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="card section cert-investment">
        <h3>Investment summary</h3>
        <div class="ci-row"><span>Total training investment</span><strong>{{ certAnalysis.totalInvestment }}</strong></div>
        <div class="ci-row ci-highlight"><span>Expected salary impact</span><strong>{{ certAnalysis.salaryImpact }}</strong></div>
        <div class="ci-note">ROI: Investment pays for itself within 2 months of higher salary</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.ai-career-page { padding-top: var(--space-5); padding-bottom: var(--space-8); max-width: 960px; }

/* Header */
.career-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: var(--space-4); }
.career-header h1 { font: var(--font-h1); display: flex; align-items: center; gap: var(--space-2); }
.career-sub { font: var(--font-small); color: var(--color-text-secondary); margin-top: 2px; }
.profile-context { display: flex; align-items: center; gap: var(--space-3); background: var(--color-white); padding: var(--space-3) var(--space-4); border-radius: var(--radius-lg); border: 0.5px solid var(--color-border); }
.pc-score { width: 44px; height: 44px; border-radius: 50%; border: 2.5px solid; display: flex; align-items: center; justify-content: center; font-size: 16px; font-weight: 600; flex-shrink: 0; }
.pc-rank { font: var(--font-body); font-weight: 500; display: block; }
.pc-detail { font: var(--font-caption); color: var(--color-text-secondary); }

/* Tabs */
.career-tabs { display: flex; border-bottom: 2px solid var(--color-border); margin-bottom: var(--space-5); gap: 0; }
.career-tab { padding: var(--space-3) var(--space-4); background: none; border: none; font: var(--font-small); font-weight: 500; color: var(--color-text-secondary); cursor: pointer; border-bottom: 2px solid transparent; margin-bottom: -2px; transition: color 0.15s; white-space: nowrap; }
.career-tab:hover { color: var(--color-text-primary); background: var(--color-surface); }
.career-tab.active { color: var(--color-primary); border-bottom-color: var(--color-primary); }

/* AI Banner */
.ai-banner { display: flex; align-items: center; gap: var(--space-2); padding: var(--space-3) var(--space-4); background: var(--color-primary-light); border-radius: var(--radius-md); font: var(--font-small); color: var(--color-primary-dark); margin-bottom: var(--space-4); }

/* Section */
.card { background: var(--color-white); border: 0.5px solid var(--color-border); border-radius: var(--radius-lg); overflow: hidden; }
.section { padding: var(--space-5); margin-bottom: var(--space-4); }
.section h2 { font: var(--font-h2); margin-bottom: var(--space-3); display: flex; align-items: center; gap: var(--space-2); }
.section h3 { font: var(--font-h3); margin-bottom: var(--space-3); }
.section-sub { font: var(--font-small); color: var(--color-text-tertiary); margin: -8px 0 var(--space-4) 0; }

/* Job Cards */
.job-list { display: flex; flex-direction: column; gap: var(--space-3); }
.job-card { padding: var(--space-4); transition: border-color 0.15s; }
.job-card:hover { border-color: var(--color-primary); }
.job-top { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: var(--space-2); }
.job-info h3 { font: var(--font-h3); display: flex; align-items: center; gap: var(--space-2); }
.job-meta { font: var(--font-small); color: var(--color-text-secondary); margin-top: 2px; }
.job-score { width: 52px; height: 52px; border-radius: 50%; border: 2.5px solid; display: flex; align-items: center; justify-content: center; font-size: 15px; font-weight: 600; flex-shrink: 0; }
.job-salary { font: var(--font-body); font-weight: 600; color: var(--color-success); margin-bottom: var(--space-3); }
.job-reasons { display: flex; flex-wrap: wrap; gap: var(--space-1); margin-bottom: var(--space-3); }
.reason-tag { font: var(--font-caption); background: var(--color-surface); color: var(--color-text-secondary); padding: 3px 10px; border-radius: 12px; }
.job-actions { display: flex; gap: var(--space-2); }
.badge-urgent { background: var(--color-danger-bg); color: var(--color-danger); font: var(--font-caption); font-weight: 500; padding: 2px 8px; border-radius: 4px; }

/* Company Match */
.company-match-list { display: flex; flex-direction: column; gap: var(--space-4); }
.company-match-card { padding: var(--space-4); }
.cm-top { display: flex; align-items: center; gap: var(--space-3); margin-bottom: var(--space-3); }
.cm-avatar { width: 44px; height: 44px; border-radius: var(--radius-md); display: flex; align-items: center; justify-content: center; color: white; font-weight: 600; font-size: 14px; flex-shrink: 0; }
.cm-info { flex: 1; }
.cm-info h3 { font: var(--font-h3); margin-bottom: 0; }
.cm-meta { font: var(--font-small); color: var(--color-text-secondary); }
.cm-score { width: 52px; height: 52px; border-radius: 50%; border: 2.5px solid; display: flex; align-items: center; justify-content: center; font-size: 15px; font-weight: 600; flex-shrink: 0; }
.cm-stats { display: grid; grid-template-columns: repeat(3, 1fr); gap: var(--space-3); background: var(--color-surface); border-radius: var(--radius-md); padding: var(--space-3); margin-bottom: var(--space-3); text-align: center; }
.cm-stat-val { display: block; font: var(--font-body); font-weight: 600; color: var(--color-primary); }
.cm-stat-label { font: var(--font-caption); color: var(--color-text-tertiary); }
.cm-pros-cons { display: grid; grid-template-columns: 1fr 1fr; gap: var(--space-3); }
.cm-item { display: flex; align-items: flex-start; gap: var(--space-2); font: var(--font-small); padding: 2px 0; }
.cm-dot { width: 6px; height: 6px; border-radius: 50%; margin-top: 6px; flex-shrink: 0; }
.cm-dot-g { background: var(--color-success); }
.cm-dot-r { background: var(--color-warning); }

/* Salary Advisor */
.sa-kpis { display: grid; grid-template-columns: repeat(3, 1fr); gap: var(--space-3); }
.sa-kpi { text-align: center; padding: var(--space-4); background: var(--color-surface); border-radius: var(--radius-md); }
.sa-kpi-highlight { background: var(--color-primary-light); border: 2px solid var(--color-primary); }
.sa-kpi-label { display: block; font: var(--font-caption); color: var(--color-text-secondary); margin-bottom: 4px; }
.sa-kpi-val { display: block; font-size: 24px; font-weight: 600; }
.sa-kpi-blue { color: var(--color-primary); }
.sa-kpi-sub { display: block; font: var(--font-caption); color: var(--color-text-tertiary); margin-top: 4px; }
.tips-list { display: flex; flex-direction: column; gap: var(--space-3); }
.tip-card { display: flex; gap: var(--space-3); padding: var(--space-3); background: var(--color-surface); border-radius: var(--radius-md); }
.tip-num { width: 28px; height: 28px; background: var(--color-primary); color: white; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 13px; font-weight: 600; flex-shrink: 0; }
.tip-text { font: var(--font-body); margin-bottom: 4px; }
.tip-impact { font: var(--font-caption); color: var(--color-success); }
.sa-potential { text-align: center; }
.sa-pot-label { font: var(--font-small); color: var(--color-text-secondary); }
.sa-pot-val { font-size: 32px; font-weight: 600; color: var(--color-primary); margin: var(--space-2) 0; }
.sa-pot-increase { font: var(--font-body); color: var(--color-success); font-weight: 500; }

/* Career Roadmap */
.roadmap-checklist { display: flex; flex-direction: column; gap: var(--space-3); }
.rm-item { display: flex; gap: var(--space-3); padding: var(--space-3); border-radius: var(--radius-md); }
.rm-done { background: var(--color-success-bg); }
.rm-action { background: var(--color-warning-bg); }
.rm-pending { background: var(--color-surface); }
.rm-icon { width: 28px; height: 28px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 13px; flex-shrink: 0; }
.rm-check { background: var(--color-success); color: white; width: 28px; height: 28px; border-radius: 50%; display: flex; align-items: center; justify-content: center; }
.rm-action { background: var(--color-warning); color: white; width: 28px; height: 28px; border-radius: 50%; display: flex; align-items: center; justify-content: center; }
.rm-pending { color: var(--color-text-tertiary); }
.rm-body strong { font: var(--font-body); font-weight: 500; display: block; }
.rm-body p { font: var(--font-small); color: var(--color-text-secondary); margin-top: 2px; }
.career-timeline { position: relative; padding-left: var(--space-5); }
.career-timeline::before { content: ''; position: absolute; left: 11px; top: 8px; bottom: 8px; width: 2px; background: var(--color-border); }
.ct-item { position: relative; margin-bottom: var(--space-5); }
.ct-dot { position: absolute; left: calc(-1 * var(--space-5) + 4px); top: 6px; width: 16px; height: 16px; border-radius: 50%; background: var(--color-primary); border: 3px solid var(--color-primary-light); }
.ct-body h3 { font: var(--font-h3); margin-bottom: 2px; }
.ct-meta { font: var(--font-small); color: var(--color-primary); font-weight: 500; }
.ct-req { font: var(--font-caption); color: var(--color-text-secondary); margin-top: 4px; }

/* Certificate Analysis */
.cert-list { display: flex; flex-direction: column; gap: var(--space-2); }
.cert-item { display: flex; align-items: flex-start; gap: var(--space-3); padding: var(--space-3); border-radius: var(--radius-md); }
.cert-expiring { background: var(--color-warning-bg); }
.cert-missing { background: var(--color-surface); }
.cert-valid { background: var(--color-success-bg); }
.cert-badge { font: var(--font-caption); font-weight: 500; padding: 2px 8px; border-radius: 4px; text-transform: uppercase; letter-spacing: 0.5px; }
.cb-urgent { background: var(--color-danger); color: white; }
.cb-warning { background: var(--color-warning); color: white; }
.cb-recommended { background: var(--color-primary); color: white; }
.cb-career { background: var(--color-accent); color: white; }
.cb-ok { background: var(--color-success); color: white; }
.cert-info { flex: 1; }
.cert-info strong { font: var(--font-body); font-weight: 500; display: block; }
.cert-info span { font: var(--font-small); color: var(--color-text-secondary); }
.cert-benefit { font: var(--font-caption); color: var(--color-primary); margin-top: 4px; font-weight: 500; }
.cert-investment { text-align: center; }
.ci-row { display: flex; justify-content: space-between; padding: var(--space-3) 0; border-bottom: 1px solid var(--color-border); font: var(--font-body); }
.ci-row:last-of-type { border-bottom: none; }
.ci-highlight strong { color: var(--color-success); font-size: 18px; }
.ci-note { font: var(--font-caption); color: var(--color-success); margin-top: var(--space-3); font-weight: 500; }

.btn-sm { font-size: 12px; padding: 6px 14px; }
[title] { cursor: help; }

@media (max-width: 768px) {
  .career-header { flex-direction: column; gap: var(--space-3); }
  .career-tabs { overflow-x: auto; }
  .career-tab { padding: var(--space-2) var(--space-3); font-size: 12px; }
  .cm-pros-cons { grid-template-columns: 1fr; }
  .sa-kpis { grid-template-columns: 1fr; }
}
</style>
