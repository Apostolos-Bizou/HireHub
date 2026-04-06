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
  { name: 'Safety awareness', rating: 0 },
  { name: 'Professional competence', rating: 0 },
  { name: 'Teamwork & collaboration', rating: 0 },
  { name: 'Reliability & work ethic', rating: 0 },
  { name: 'Leadership & attitude', rating: 0 }
])

const comment = ref('')
const relationship = ref('')
const submitting = ref(false)

const isValid = computed(() => {
  return criteria.value.every(c => c.rating > 0) && comment.value.trim().length >= 20 && relationship.value
})

function setRating(critIndex, starIndex) {
  criteria.value[critIndex].rating = starIndex + 1
}

function submitReview() {
  if (!isValid.value) return
  submitting.value = true
  setTimeout(() => {
    emit('submit', {
      criteria: criteria.value.map(c => ({ name: c.name, rating: c.rating })),
      comment: comment.value,
      relationship: relationship.value,
      vessel: props.verifiedVessel
    })
    submitting.value = false
  }, 1000)
}
</script>

<template>
  <div class="review-form card">
    <div class="rf-header">
      <h2>Review {{ seafarerName }}</h2>
      <p class="rf-sub">Your review will be verified against shared sea service records</p>
    </div>

    <!-- Verified Vessel Badge -->
    <div class="rf-verified">
      <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
      Verified: {{ verifiedVessel }} · {{ overlapPeriod }} · {{ overlapMonths }} months overlap
    </div>

    <!-- Star Ratings -->
    <div v-for="(c, ci) in criteria" :key="ci" class="rf-criteria-group">
      <label class="rf-label">{{ c.name }}</label>
      <div class="rf-stars">
        <span
          v-for="si in 5"
          :key="si"
          class="rf-star"
          :class="{ lit: si <= c.rating }"
          @click="setRating(ci, si - 1)"
        >★</span>
        <span v-if="c.rating > 0" class="rf-rating-text">{{ ['Poor', 'Fair', 'Good', 'Very good', 'Excellent'][c.rating - 1] }}</span>
      </div>
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

.rf-criteria-group { margin-bottom: var(--space-3); }
.rf-label {
  display: block;
  font: var(--font-small);
  font-weight: 500;
  color: var(--color-text-secondary);
  margin-bottom: 6px;
}
.rf-required { color: var(--color-danger); }

.rf-stars { display: flex; align-items: center; gap: 4px; }
.rf-star {
  font-size: 24px;
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
  margin-left: var(--space-2);
}

.rf-group { margin-bottom: var(--space-4); }
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

.rf-notice {
  display: flex;
  align-items: flex-start;
  gap: 6px;
  font: var(--font-caption);
  color: var(--color-text-tertiary);
  line-height: 1.5;
}
.rf-notice svg { flex-shrink: 0; margin-top: 2px; }
</style>
