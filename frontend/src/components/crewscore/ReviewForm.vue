<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  seafarerName: { type: String, default: 'Simos Varias' },
  verifiedVessel: { type: String, default: 'MT Aegean Warrior' },
  overlapPeriod: { type: String, default: 'Jan 2025 – Feb 2026' },
  overlapMonths: { type: Number, default: 14 }
})

const emit = defineEmits(['submit', 'cancel'])

const criteria = ref([
  {
    name: 'Safety awareness', weight: '25%', expanded: false,
    sub: [
      { name: 'Τήρηση κανονισμών ασφαλείας', nameEn: 'Safety regulation compliance', rating: 0 },
      { name: 'ISM / ISPS γνώσεις', nameEn: 'ISM / ISPS knowledge', rating: 0 },
      { name: 'Emergency readiness', nameEn: 'Emergency preparedness & drills', rating: 0 }
    ]
  },
  {
    name: 'Professional competence', weight: '25%', expanded: false,
    sub: [
      { name: 'Γνώσεις', nameEn: 'Technical knowledge', rating: 0 },
      { name: 'Τεχνική ικανότητα', nameEn: 'Hands-on skills', rating: 0 },
      { name: 'Ποιότητα εργασίας', nameEn: 'Quality of work', rating: 0 }
    ]
  },
  {
    name: 'Teamwork & collaboration', weight: '20%', expanded: false,
    sub: [
      { name: 'Συνεργασία', nameEn: 'Cooperation with crew', rating: 0 },
      { name: 'Επικοινωνία', nameEn: 'Communication skills', rating: 0 },
      { name: 'Υποστήριξη πληρώματος', nameEn: 'Crew support & morale', rating: 0 }
    ]
  },
  {
    name: 'Reliability & work ethic', weight: '20%', expanded: false,
    sub: [
      { name: 'Συνέπεια', nameEn: 'Punctuality & consistency', rating: 0 },
      { name: 'Αξιοπιστία', nameEn: 'Dependability & trust', rating: 0 },
      { name: 'Εργατικότητα', nameEn: 'Work ethic & dedication', rating: 0 },
      { name: 'Πειθαρχία', nameEn: 'Discipline & compliance', rating: 0 }
    ]
  },
  {
    name: 'Leadership & attitude', weight: '10%', expanded: false,
    sub: [
      { name: 'Ηγεσία', nameEn: 'Leadership ability', rating: 0 },
      { name: 'Θετική στάση', nameEn: 'Positive attitude', rating: 0 },
      { name: 'Mentoring', nameEn: 'Mentoring junior crew', rating: 0 },
      { name: 'Διαχείριση πίεσης', nameEn: 'Stress management', rating: 0 }
    ]
  }
])

const comment = ref('')
const relationship = ref('')
const submitting = ref(false)

// Computed average per criterion
function critAvg(crit) {
  const rated = crit.sub.filter(s => s.rating > 0)
  if (rated.length === 0) return 0
  return (rated.reduce((a, s) => a + s.rating, 0) / rated.length).toFixed(1)
}

function critComplete(crit) {
  return crit.sub.every(s => s.rating > 0)
}

function critPartial(crit) {
  return crit.sub.some(s => s.rating > 0) && !critComplete(crit)
}

const allRated = computed(() => criteria.value.every(c => critComplete(c)))
const isValid = computed(() => allRated.value && comment.value.trim().length >= 20 && relationship.value)

// Overall weighted score
const overallScore = computed(() => {
  const weights = [0.25, 0.25, 0.20, 0.20, 0.10]
  let total = 0, wTotal = 0
  criteria.value.forEach((c, i) => {
    const avg = parseFloat(critAvg(c))
    if (avg > 0) { total += avg * weights[i]; wTotal += weights[i] }
  })
  return wTotal > 0 ? (total / wTotal).toFixed(1) : '—'
})

function setSubRating(critIndex, subIndex, star) {
  criteria.value[critIndex].sub[subIndex].rating = star
}

function toggleExpand(critIndex) {
  criteria.value[critIndex].expanded = !criteria.value[critIndex].expanded
}

function submitReview() {
  if (!isValid.value) return
  submitting.value = true
  setTimeout(() => {
    emit('submit', {
      criteria: criteria.value.map(c => ({
        name: c.name,
        rating: parseFloat(critAvg(c)),
        subRatings: c.sub.map(s => ({ name: s.name, rating: s.rating }))
      })),
      overallScore: overallScore.value,
      comment: comment.value,
      relationship: relationship.value,
      vessel: props.verifiedVessel
    })
    submitting.value = false
  }, 1000)
}

const ratingLabels = ['', 'Poor', 'Fair', 'Good', 'Very good', 'Excellent']
</script>

<template>
  <div class="review-form card">
    <div class="rf-header">
      <h2>Review {{ seafarerName }}</h2>
      <p class="rf-sub">Rate each sub-criterion. Your review will be verified against shared sea service records.</p>
    </div>

    <!-- Verified Vessel Badge -->
    <div class="rf-verified">
      <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
      Verified: {{ verifiedVessel }} · {{ overlapPeriod }} · {{ overlapMonths }} months overlap
    </div>

    <!-- Criteria Accordion -->
    <div v-for="(c, ci) in criteria" :key="ci" class="rf-criterion">
      <div class="rf-crit-header" @click="toggleExpand(ci)">
        <div class="rf-crit-left">
          <span class="rf-expand-icon">{{ c.expanded ? '▾' : '▸' }}</span>
          <strong>{{ c.name }}</strong>
          <span class="rf-weight">{{ c.weight }}</span>
        </div>
        <div class="rf-crit-right">
          <span v-if="critComplete(c)" class="rf-crit-status rf-crit-done">✓ {{ critAvg(c) }}/5</span>
          <span v-else-if="critPartial(c)" class="rf-crit-status rf-crit-partial">{{ c.sub.filter(s => s.rating > 0).length }}/{{ c.sub.length }}</span>
          <span v-else class="rf-crit-status rf-crit-empty">{{ c.sub.length }} items</span>
        </div>
      </div>

      <!-- Sub-criteria (expanded) -->
      <div v-if="c.expanded" class="rf-subs">
        <div v-for="(s, si) in c.sub" :key="si" class="rf-sub-row">
          <div class="rf-sub-label">
            <span class="rf-sub-name">{{ s.name }}</span>
            <span class="rf-sub-en">{{ s.nameEn }}</span>
          </div>
          <div class="rf-sub-stars">
            <span
              v-for="star in 5"
              :key="star"
              class="rf-star"
              :class="{ lit: star <= s.rating }"
              @click="setSubRating(ci, si, star)"
            >★</span>
            <span v-if="s.rating > 0" class="rf-rating-text">{{ ratingLabels[s.rating] }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Overall Score Preview -->
    <div v-if="allRated" class="rf-overall">
      <span class="rf-overall-label">Overall weighted score:</span>
      <span class="rf-overall-val">{{ overallScore }} / 5</span>
    </div>

    <!-- Comment -->
    <div class="rf-group">
      <label class="rf-label">Your experience working with {{ seafarerName }} <span class="rf-required">*</span></label>
      <textarea
        v-model="comment"
        class="rf-textarea"
        placeholder="Describe your experience working with this colleague. What stood out? How was the collaboration on board? (minimum 20 characters)"
        rows="4"
      ></textarea>
      <span class="rf-char-count" :class="{ 'rf-char-ok': comment.length >= 20 }">{{ comment.length }} / 20 min</span>
    </div>

    <!-- Relationship -->
    <div class="rf-group">
      <label class="rf-label">Your relationship <span class="rf-required">*</span></label>
      <select v-model="relationship" class="rf-select">
        <option value="">Select relationship...</option>
        <option value="supervisor">Direct supervisor (Captain / Chief Officer)</option>
        <option value="peer">Same rank / peer</option>
        <option value="junior">Junior officer I supervised</option>
        <option value="other_dept">Different department</option>
      </select>
    </div>

    <!-- Actions -->
    <div class="rf-actions">
      <button
        class="btn btn-primary rf-submit"
        :disabled="!isValid || submitting"
        @click="submitReview"
      >
        {{ submitting ? 'Submitting...' : 'Submit review' }}
      </button>
      <button class="btn btn-tertiary" @click="emit('cancel')">Cancel</button>
    </div>

    <!-- Validation Summary -->
    <div v-if="!allRated" class="rf-validation">
      <span v-for="(c, ci) in criteria" :key="ci">
        <span v-if="!critComplete(c)" class="rf-val-item">{{ c.name }}: {{ c.sub.filter(s => s.rating > 0).length }}/{{ c.sub.length }} rated</span>
      </span>
    </div>

    <!-- Notice -->
    <div class="rf-notice">
      <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg>
      Reviews are moderated by AI for authenticity. Fake or malicious reviews will be removed and may result in account suspension.
    </div>
  </div>
</template>

<style scoped>
.review-form { padding: var(--space-5); }
.rf-header { margin-bottom: var(--space-4); }
.rf-header h2 { font: var(--font-h2); }
.rf-sub { font: var(--font-small); color: var(--color-text-secondary); margin-top: 2px; }

.rf-verified {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font: var(--font-caption);
  font-weight: 500;
  padding: 6px 12px;
  background: var(--color-success-bg);
  color: var(--color-success);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-4);
}

/* Criterion Accordion */
.rf-criterion {
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-2);
  overflow: hidden;
}
.rf-crit-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-3) var(--space-4);
  cursor: pointer;
  background: var(--color-surface);
  transition: background 0.15s;
}
.rf-crit-header:hover { background: var(--color-primary-light); }
.rf-crit-left { display: flex; align-items: center; gap: var(--space-2); }
.rf-crit-left strong { font: var(--font-body); font-weight: 500; }
.rf-expand-icon { font-size: 12px; color: var(--color-text-tertiary); width: 14px; }
.rf-weight { font: var(--font-caption); color: var(--color-text-tertiary); background: var(--color-white); padding: 1px 6px; border-radius: 4px; }

.rf-crit-status { font: var(--font-caption); font-weight: 500; padding: 2px 8px; border-radius: 10px; }
.rf-crit-done { background: var(--color-success-bg); color: var(--color-success); }
.rf-crit-partial { background: var(--color-warning-bg); color: var(--color-warning); }
.rf-crit-empty { background: var(--color-surface); color: var(--color-text-tertiary); }

/* Sub-criteria */
.rf-subs { padding: var(--space-2) var(--space-4) var(--space-3); }
.rf-sub-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-2) 0;
  border-bottom: 1px solid var(--color-border);
}
.rf-sub-row:last-child { border-bottom: none; }
.rf-sub-label { flex: 1; }
.rf-sub-name { font: var(--font-small); font-weight: 500; display: block; }
.rf-sub-en { font: var(--font-caption); color: var(--color-text-tertiary); }

.rf-sub-stars { display: flex; align-items: center; gap: 3px; flex-shrink: 0; }
.rf-star {
  font-size: 22px;
  cursor: pointer;
  color: var(--color-border);
  transition: color 0.1s, transform 0.1s;
  user-select: none;
}
.rf-star:hover { transform: scale(1.15); }
.rf-star.lit { color: #E7A33E; }
.rf-rating-text {
  font: var(--font-caption);
  color: var(--color-text-secondary);
  margin-left: var(--space-1);
  min-width: 55px;
}

/* Overall Score */
.rf-overall {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-3) var(--space-4);
  background: var(--color-primary-light);
  border: 2px solid var(--color-primary);
  border-radius: var(--radius-md);
  margin: var(--space-4) 0;
}
.rf-overall-label { font: var(--font-body); font-weight: 500; }
.rf-overall-val { font-size: 22px; font-weight: 600; color: var(--color-primary); }

/* Form fields */
.rf-group { margin-bottom: var(--space-4); }
.rf-label {
  display: block;
  font: var(--font-small);
  font-weight: 500;
  color: var(--color-text-secondary);
  margin-bottom: 6px;
}
.rf-required { color: var(--color-danger); }

.rf-textarea {
  width: 100%;
  padding: var(--space-3);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font: var(--font-body);
  resize: vertical;
  min-height: 100px;
  outline: none;
  transition: border-color 0.15s;
}
.rf-textarea:focus { border-color: var(--color-primary); }
.rf-char-count {
  display: block;
  font: var(--font-caption);
  color: var(--color-text-tertiary);
  margin-top: 4px;
  text-align: right;
}
.rf-char-ok { color: var(--color-success); }

.rf-select {
  width: 100%;
  padding: var(--space-2) var(--space-3);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font: var(--font-body);
  background: var(--color-white);
  outline: none;
  cursor: pointer;
}

.rf-actions {
  display: flex;
  gap: var(--space-2);
  margin-bottom: var(--space-3);
}
.rf-submit { padding: 10px 24px; }
.rf-submit:disabled { opacity: 0.5; cursor: not-allowed; }

.rf-validation {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
  margin-bottom: var(--space-3);
}
.rf-val-item {
  font: var(--font-caption);
  color: var(--color-warning);
  background: var(--color-warning-bg);
  padding: 2px 8px;
  border-radius: 4px;
}

.rf-notice {
  display: flex;
  align-items: flex-start;
  gap: 6px;
  font: var(--font-caption);
  color: var(--color-text-tertiary);
  line-height: 1.5;
}
.rf-notice svg { flex-shrink: 0; margin-top: 2px; }

@media (max-width: 768px) {
  .rf-sub-row { flex-direction: column; align-items: flex-start; gap: var(--space-2); }
  .rf-crit-header { flex-direction: column; align-items: flex-start; gap: var(--space-2); }
}
</style>
